package com.hamming.storim.client.controller;

import com.hamming.storim.client.ImageUtils;
import com.hamming.storim.client.STORIMWindowController;
import com.hamming.storim.client.listitem.TileSetEditorListItem;
import com.hamming.storim.client.panels.TileSetEditorPanel;
import com.hamming.storim.client.view.TileSet;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.TileSetDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.*;
import com.hamming.storim.common.dto.protocol.serverpush.SetCurrentUserDTO;
import com.hamming.storim.common.dto.protocol.serverpush.TileSetAddedDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;

import javax.swing.*;
import java.awt.*;

public class TileSetEditorPanelController implements ConnectionListener {

    private ConnectionController connectionController;
    private TileSetEditorPanel panel;
    private STORIMWindowController windowController;
    private DefaultListModel<TileSetEditorListItem> tileSetModel = new DefaultListModel<>();

    boolean newTileSet = false;
    private UserDto currentUser;


    public TileSetEditorPanelController(STORIMWindowController windowController, TileSetEditorPanel panel, ConnectionController connectionController) {
        this.panel = panel;
        this.windowController = windowController;
        this.connectionController = connectionController;
        connectionController.addConnectionListener(this);
        registerReceivers();
        setup();
    }


    private void registerReceivers() {
        connectionController.registerReceiver(SetCurrentUserDTO.class, (ProtocolReceiver<SetCurrentUserDTO>) dto -> setCurrentUser(dto));
        connectionController.registerReceiver(LoginResultDTO.class, (ProtocolReceiver<LoginResultDTO>) dto -> loginSuccess(dto.isSuccess()));
        connectionController.registerReceiver(TileSetAddedDTO.class, (ProtocolReceiver<TileSetAddedDTO>) dto -> addTileSet(dto.getTileSetDto()));
    }


    private void setup() {
        tileSetModel = new DefaultListModel<>();
        panel.getListTileSets().setModel(tileSetModel);
        panel.getListTileSets().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { //Else this is called twice!
                TileSetEditorListItem item = panel.getListTileSets().getSelectedValue();
                tileSetSelected(item.getId());
            }
        });


        panel.getBtnDelete().addActionListener(e -> deleteTileSet());
        panel.getBtnSave().addActionListener(e -> saveTileSet());
        setEditable(false);
        panel.getBtnCreate().addActionListener(e -> createTileSet());
        panel.getBtnSave().setEnabled(false);
        panel.getBtnDelete().setEnabled(false);
        panel.getBtnCreate().setEnabled(false);
        panel.getLblImagePreview().setText("");
    }

    private void setCurrentUser(SetCurrentUserDTO dto) {
        currentUser = dto.getUser();
        panel.getBtnCreate().setEnabled(true);
    }

    private void loginSuccess(boolean loginSucceeded) {
        if (loginSucceeded) {
            //Get the Tilesets
            GetTileSetsDTO getTileSetsDTO = new GetTileSetsDTO();
            GetTileSetsResponseDTO getTileSetsResponseDTO = connectionController.sendReceive(getTileSetsDTO, GetTileSetsResponseDTO.class);
            if (getTileSetsResponseDTO.getTileSets() != null) {
                for (Long tileSetId : getTileSetsResponseDTO.getTileSets()) {
                    TileSetDto tileSetDto = getTileSet(tileSetId);
                    addTileSet(tileSetDto);
                }
            }
        }
    }

    private TileSetDto getTileSet(Long tileSetId) {
        GetTileSetDTO getTileSetDTO = new GetTileSetDTO(tileSetId);
        GetTileSetResultDTO getTileSetResultDTO = connectionController.sendReceive(getTileSetDTO, GetTileSetResultDTO.class);
        return getTileSetResultDTO.getTileSetDto();
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
        SwingUtilities.invokeLater(() -> {
            panel.getLblId().setText("");
            panel.getTxtTileSetName().setText("");
            panel.getTxtTileWidth().setText("10");
            panel.getTxtTileHeight().setText("10");
            panel.getBtnSave().setEnabled(false);
            panel.getBtnDelete().setEnabled(false);
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
        });
    }


    private void removeTileSet(Long id) {
//        TileSetEditorListItem found = null;
//        for (int i = 0; i < tileSetModel.getSize(); i++) {
//            TileSetEditorListItem item = tileSetModel.getElementAt(i);
//            if (item != null && item.getId().equals(id)) {
//                found = item;
//                break;
//            }
//        }
//        if (found != null) {
//            tileSetModel.removeElement(found);
//        }
    }

    public void addTileSet(TileSetDto dto) {
        SwingUtilities.invokeLater(() -> {
        //    removeTileSet(dto.getId());
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
//        Long roomId = Long.valueOf(panel.getLblID().getText());
//        DeleteTileSetDTO deleteRoomDTO = new DeleteRoomDTO(roomId);
//        connectionController.send(deleteRoomDTO);
//        //FIXME Delete Room
//        empty(false);
    }

    private void createTileSet() {
        newTileSet = true;
        SwingUtilities.invokeLater(() -> {
            panel.getLblId().setText("");
            panel.getTxtTileSetName().setText("New TILESET Name");
            panel.getBtnSave().setEnabled(true);
            panel.getListTileSets().clearSelection();
            panel.getBtnDelete().setEnabled(false);
            panel.getTxtTileSetName().setEnabled(true);
            panel.getTxtTileHeight().setEnabled(true);
            panel.getTxtTileWidth().setEnabled(true);
        });
    }

    private void saveTileSet() {
        String roomName = panel.getTxtTileSetName().getText().trim();
        int width = Integer.valueOf(panel.getTxtTileWidth().getText());
        int height = Integer.valueOf(panel.getTxtTileHeight().getText());

        if (newTileSet) {
//            AddRoomDto addRoomDto = new AddRoomDto(roomName, rows, cols);
//            connectionController.send(addRoomDto);
        } else {
            // Update tileSet!
//            Long roomId = Long.valueOf(panel.getLblId().getText());
//
//            UpdateRoomDto updateRoomDto = new UpdateRoomDto(roomId, roomName, rows, cols, null, null, null, null);
//            connectionController.send(updateRoomDto);

        }


        setEditable(false);
        empty(false);
        panel.getListTileSets().clearSelection();
        panel.getBtnDelete().setEnabled(false);
    }

    private void tileSetSelected(Long tileId) {
        // Get TileSet details
        TileSetDto tileSetDto = getTileSet(tileId);
        TileSet tileSet = new TileSet(tileSetDto);
        Image tileSetImage = ImageUtils.decode(tileSetDto.getImageData());
        Image iconImage = tileSetImage.getScaledInstance(panel.getLblImagePreview().getWidth(), panel.getLblImagePreview().getHeight(), Image.SCALE_SMOOTH);

        //Then update
        SwingUtilities.invokeLater(() -> {
            panel.getCmbTiles().removeAllItems();
            panel.getLblId().setText(tileSetDto.getId().toString());
            panel.getTxtTileSetName().setText(tileSetDto.getName());
            panel.getTxtTileWidth().setText("" + tileSetDto.getTileWidth());
            panel.getTxtTileHeight().setText("" + tileSetDto.getTileHeight());
            panel.getLblImagePreview().setIcon(new ImageIcon(iconImage));
            for ( Image tile : tileSet.getTiles()) {
                Image tileImageIcon = ImageUtils.resize(tile, 60, 60);
                panel.getCmbTiles().addItem( new ImageIcon(tileImageIcon));
            }
            panel.getBtnSave().setEnabled(true);
            panel.getBtnDelete().setEnabled(true);
            setEditable(true);
        });
    }

}
