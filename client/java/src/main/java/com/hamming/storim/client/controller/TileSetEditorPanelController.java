package com.hamming.storim.client.controller;

import com.hamming.storim.client.ImageUtils;
import com.hamming.storim.client.STORIMWindowController;
import com.hamming.storim.client.listitem.TileSetEditorListItem;
import com.hamming.storim.client.panels.TileSetEditorPanel;
import com.hamming.storim.client.view.TileSet;
import com.hamming.storim.common.MicroServerException;
import com.hamming.storim.common.MicroServerProxy;
import com.hamming.storim.common.dto.TileSetDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginWithTokenResultDTO;
import com.hamming.storim.common.dto.protocol.serverpush.SetCurrentUserDTO;
import com.hamming.storim.common.dto.protocol.serverpush.TileSetAddedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.TileSetDeletedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.TileSetUpdatedDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class TileSetEditorPanelController implements ConnectionListener {

    private MicroServerProxy microServerProxy;
    private TileSetEditorPanel panel;
    private STORIMWindowController windowController;
    private DefaultListModel<TileSetEditorListItem> tileSetModel = new DefaultListModel<>();
    boolean newTileSet = false;
    private Image tileSetImage;
    private UserDto currentUser;
    private JFileChooser fileChooser;
    private List<Long> editors;
    private TileSetDto selectedTileSetDto;

    public TileSetEditorPanelController(STORIMWindowController windowController, TileSetEditorPanel panel, MicroServerProxy microServerProxy) {
        this.panel = panel;
        this.windowController = windowController;
        this.microServerProxy = microServerProxy;
        this.fileChooser = new JFileChooser();
        editors = null;
        microServerProxy.getConnectionController().addConnectionListener(this);
        registerReceivers();
        setup();
    }


    private void registerReceivers() {
        microServerProxy.getConnectionController().registerReceiver(SetCurrentUserDTO.class, (ProtocolReceiver<SetCurrentUserDTO>) dto -> setCurrentUser(dto));
        microServerProxy.getConnectionController().registerReceiver(TileSetAddedDTO.class, (ProtocolReceiver<TileSetAddedDTO>) dto -> addTileSet(dto.getTileSetDto()));
        microServerProxy.getConnectionController().registerReceiver(TileSetUpdatedDTO.class, (ProtocolReceiver<TileSetUpdatedDTO>) dto -> addTileSet(dto.getTileSetDto()));
        microServerProxy.getConnectionController().registerReceiver(TileSetDeletedDTO.class, (ProtocolReceiver<TileSetDeletedDTO>) dto -> tileSetDeleted(dto.getId()));
    }

    private void setup() {
        tileSetModel = new DefaultListModel<>();
        panel.getListTileSets().setModel(tileSetModel);
        panel.getListTileSets().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { //Else this is called twice!
                TileSetEditorListItem item = panel.getListTileSets().getSelectedValue();
                if (item != null ) {
                    tileSetSelected(item.getId());
                }
            }
        });

        panel.getBtnRecalculate().addActionListener(e -> {
            widthHeightChanged();
        });
        panel.getBtnLoadTileSetImage().addActionListener(e -> {
            chooseFile();
        });
        panel.getBtnEditAuthorisation().addActionListener(e -> {
            openAuthorisationWindow();
        });

        panel.getBtnDelete().addActionListener(e -> deleteTileSet());
        panel.getBtnSave().addActionListener(e -> saveTileSet());
        setEditable(false);
        panel.getBtnCreate().addActionListener(e -> createTileSet());
        panel.getBtnSave().setEnabled(false);
        panel.getBtnDelete().setEnabled(false);
        panel.getBtnCreate().setEnabled(false);
        panel.getBtnEditAuthorisation().setEnabled(false);
        panel.getLblImagePreview().setText("");
    }

    private void openAuthorisationWindow() {
        AuthorisationPanelController.showAuthorisationPanel(panel, selectedTileSetDto, microServerProxy);
    }

    private void widthHeightChanged() {
        int width = Integer.valueOf(panel.getTxtTileWidth().getText().trim());
        int height = Integer.valueOf(panel.getTxtTileHeight().getText().trim());
        String name = panel.getTxtTileSetName().getText().trim();
        byte[] imagedata = ImageUtils.encode(tileSetImage);
        TileSetDto dto = new TileSetDto(null, name, imagedata, width, height );
        updateTilesInCombobox(dto);
    }

    private void setCurrentUser(SetCurrentUserDTO dto) {
        currentUser = dto.getUser();
        panel.getBtnCreate().setEnabled(true);
        tileSetModel.removeAllElements();

        //Get the Tilesets for the user
        try {
            List<Long> tileSetIds = microServerProxy.getTileSetsForUser(currentUser.getId());
            for (Long tileSetId : tileSetIds) {
                TileSetDto tileSetDto = getTileSet(tileSetId);
                addTileSet(tileSetDto);
            }
        } catch (MicroServerException e) {
            JOptionPane.showMessageDialog(panel, e.getMessage());
        }
    }

    private TileSetDto getTileSet(Long tileSetId) {
        TileSetDto tileSetDto = null;
        try {
            tileSetDto =  microServerProxy.getTileSet(tileSetId);
        } catch (MicroServerException e) {
            JOptionPane.showMessageDialog(panel, e.getMessage());
        }
        return tileSetDto;
    }

    @Override
    public void connected() {
        panel.getBtnCreate().setEnabled(true);
    }

    @Override
    public void disconnected() {
        empty(true);
        setEditable(false);
        panel.getBtnCreate().setEnabled(false);
    }


    private void empty(boolean thorough) {
        newTileSet = false;
        selectedTileSetDto = null;
        tileSetImage = null;
        SwingUtilities.invokeLater(() -> {
            panel.getLblId().setText("");
            panel.getTxtTileSetName().setText("");
            panel.getTxtTileWidth().setText("10");
            panel.getTxtTileHeight().setText("10");
            panel.getLblImagePreview().setIcon(null);
            panel.getCmbTiles().removeAllItems();
            panel.getBtnSave().setEnabled(false);
            panel.getBtnDelete().setEnabled(false);
            panel.getBtnEditAuthorisation().setEnabled(false);
            if (thorough) {
               tileSetModel.removeAllElements();
            }
        });
    }

    private int findIndex(Long tileSetID) {
        int index = -1;
        for (int i = 0; i < tileSetModel.getSize(); i++) {
            if (tileSetModel.get(i).getId().equals(tileSetID)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void setEditable(boolean editable) {
        SwingUtilities.invokeLater(() -> {
            panel.getTxtTileSetName().setEnabled(editable);
            panel.getTxtTileWidth().setEnabled(editable);
            panel.getTxtTileHeight().setEnabled(editable);
            panel.getBtnLoadTileSetImage().setEnabled(editable);
            panel.getBtnRecalculate().setEnabled(editable);
        });
    }


    private void removeTileSet(Long id) {
        TileSetEditorListItem found = null;
        for (int i = 0; i < tileSetModel.getSize(); i++) {
            TileSetEditorListItem item = tileSetModel.getElementAt(i);
            if (item != null && item.getId().equals(id)) {
                found = item;
                break;
            }
        }
        if (found != null) {
            tileSetModel.removeElement(found);
        }
    }

    public void addTileSet(TileSetDto dto) {
        SwingUtilities.invokeLater(() -> {
            removeTileSet(dto.getId());
            TileSetEditorListItem newItem = new TileSetEditorListItem(dto.getId(), dto.getName());
            tileSetModel.addElement(newItem);
        });
    }

    public void tileSetDeleted(Long id) {
        removeTileSet(id);
    }

    public void tileSetUpdated(TileSetDto dto) {
        SwingUtilities.invokeLater(() -> {
            removeTileSet(dto.getId());
            tileSetModel.addElement(new TileSetEditorListItem(dto.getId(),dto.getName()));
        });
    }

    private void deleteTileSet() {
        Long id = Long.valueOf(panel.getLblId().getText());
        microServerProxy.deleteTileSet(id);
        empty(false);
    }

    private void createTileSet() {
        newTileSet = true;
        SwingUtilities.invokeLater(() -> {
            panel.getLblId().setText("");
            panel.getTxtTileSetName().setText("New TILESET Name");
            panel.getBtnSave().setEnabled(true);
            panel.getListTileSets().clearSelection();
            panel.getBtnDelete().setEnabled(false);
            panel.getBtnRecalculate().setEnabled(true);
            panel.getBtnLoadTileSetImage().setEnabled(true);
            panel.getTxtTileSetName().setEnabled(true);
            panel.getTxtTileHeight().setEnabled(true);
            panel.getTxtTileWidth().setEnabled(true);
            panel.getLblImagePreview().setIcon(null);
            panel.getCmbTiles().removeAllItems();
            panel.getBtnEditAuthorisation().setEnabled(true);
        });
    }

    private void saveTileSet() {
        String roomName = panel.getTxtTileSetName().getText().trim();
        int tileWidth = Integer.valueOf(panel.getTxtTileWidth().getText());
        int tileHeight = Integer.valueOf(panel.getTxtTileHeight().getText());
        byte[] tileSetImageData = ImageUtils.encode(tileSetImage);

        if (newTileSet) {
            microServerProxy.addTileSet(roomName, tileWidth, tileHeight, tileSetImageData);
        } else {
            // Update tileSet!
            Long id = Long.valueOf(panel.getLblId().getText());
            microServerProxy.updateTileSet(id, roomName, tileWidth, tileHeight, tileSetImageData);
        }

        setEditable(false);
        empty(false);
        panel.getListTileSets().clearSelection();
        panel.getBtnDelete().setEnabled(false);
    }

    private void tileSetSelected(Long tileId) {
        // Get TileSet details
        selectedTileSetDto = getTileSet(tileId);
        editors = selectedTileSetDto.getEditors();
        tileSetImage = ImageUtils.decode(selectedTileSetDto.getImageData());
        Image iconImage = tileSetImage.getScaledInstance(panel.getLblImagePreview().getWidth(), panel.getLblImagePreview().getHeight(), Image.SCALE_SMOOTH);
        //Then update
        SwingUtilities.invokeLater(() -> {
            panel.getLblId().setText(selectedTileSetDto.getId().toString());
            panel.getTxtTileSetName().setText(selectedTileSetDto.getName());
            panel.getTxtTileWidth().setText("" + selectedTileSetDto.getTileWidth());
            panel.getTxtTileHeight().setText("" + selectedTileSetDto.getTileHeight());
            panel.getLblImagePreview().setIcon(new ImageIcon(iconImage));
            panel.getBtnSave().setEnabled(true);
            panel.getBtnDelete().setEnabled(true);
            panel.getBtnEditAuthorisation().setEnabled(true);
            setEditable(true);
        });
        updateTilesInCombobox(selectedTileSetDto);
    }

    private void updateTilesInCombobox(TileSetDto tileSetDto) {
        TileSet tileSet = new TileSet(tileSetDto);
        SwingUtilities.invokeLater(() -> {
            panel.getCmbTiles().removeAllItems();
            for ( Image tile : tileSet.getTiles()) {
                Image tileImageIcon = ImageUtils.resize(tile, 60, 60);
                panel.getCmbTiles().addItem( new ImageIcon(tileImageIcon));
            }
        });
    }

    private void chooseFile() {
        int returnVal = fileChooser.showOpenDialog(panel);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                tileSetImage = ImageIO.read(file);
                SwingUtilities.invokeLater(() -> {
                    Image iconImage = tileSetImage.getScaledInstance(panel.getLblImagePreview().getWidth(), panel.getLblImagePreview().getHeight(), Image.SCALE_SMOOTH);
                    panel.getLblImagePreview().setIcon(new ImageIcon(iconImage));
                });
                widthHeightChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
