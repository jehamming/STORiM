package com.hamming.storim.client.controller;

import com.hamming.storim.client.ImageUtils;
import com.hamming.storim.client.STORIMWindow;
import com.hamming.storim.client.listitem.AvatarListItem;
import com.hamming.storim.client.listitem.RoomDetailsListItem;
import com.hamming.storim.client.panels.RoomEditorPanel;
import com.hamming.storim.client.panels.TileRenderer;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.protocol.request.AddRoomDto;
import com.hamming.storim.common.dto.protocol.request.DeleteRoomDTO;
import com.hamming.storim.common.dto.protocol.request.UpdateRoomDto;
import com.hamming.storim.common.dto.protocol.requestresponse.*;
import com.hamming.storim.common.dto.protocol.serverpush.SetCurrentUserDTO;
import com.hamming.storim.common.dto.protocol.serverpush.old.RoomAddedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.old.RoomDeletedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.old.RoomUpdatedDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class RoomEditorPanelController implements ConnectionListener {

    private ConnectionController connectionController;
    private RoomEditorPanel panel;
    private STORIMWindow storimWindow;
    private DefaultListModel<AvatarListItem> avatarModel = new DefaultListModel<>();
    private DefaultListModel<RoomDetailsListItem> roomsModel = new DefaultListModel<>();
    private DefaultListModel<TileDto> tilesModel = new DefaultListModel<>();
    boolean newRoom = false;
    private JFileChooser fileChooser;
    private BufferedImage tileImage;
    private TileDto chosenTile;


    public RoomEditorPanelController(STORIMWindow storimWindow, RoomEditorPanel panel, ConnectionController connectionController) {
        this.panel = panel;
        this.storimWindow = storimWindow;
        this.connectionController = connectionController;
        this.fileChooser = new JFileChooser();
        connectionController.addConnectionListener(this);
        registerReceivers();
        setup();
    }


    private void registerReceivers() {
        connectionController.registerReceiver(SetCurrentUserDTO.class, (ProtocolReceiver<SetCurrentUserDTO>) dto -> setCurrentUser(dto));
        connectionController.registerReceiver(RoomAddedDTO.class, (ProtocolReceiver<RoomAddedDTO>) dto -> roomAdded(dto.getRoom()));
        connectionController.registerReceiver(RoomUpdatedDTO.class, (ProtocolReceiver<RoomUpdatedDTO>) dto -> roomUpdated(dto.getRoom()));
        connectionController.registerReceiver(RoomDeletedDTO.class, (ProtocolReceiver<RoomDeletedDTO>) dto -> roomDeleted(dto.getRoomId()));
    }

    private void setup() {
        panel.getListTiles().setModel(tilesModel);
        panel.getListRooms().setModel(roomsModel);
        panel.getListRooms().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { //Else this is called twice!
                RoomDetailsListItem item = panel.getListRooms().getSelectedValue();
                if (item != null && item.getRoomDto() != null) {
                    roomSelected(item.getRoomDto());
                }
            }
        });
        panel.getBtnDelete().addActionListener(e -> deleteRoom());
        panel.getBtnSave().addActionListener(e -> saveRoom());
        setEditable(false);
        panel.getBtnCreate().addActionListener(e -> createRoom());
        panel.getBtnTeleport().addActionListener(e -> teleport());
        panel.getBtnSave().setEnabled(false);
        panel.getBtnDelete().setEnabled(false);
        panel.getBtnCreate().setEnabled(false);
        panel.getBtnChooseFile().addActionListener(e -> chooseFile());
        panel.getListTiles().setCellRenderer(new TileRenderer());
        panel.getListTiles().addListSelectionListener(e -> {
            tileSelected();
        });
        panel.getListTiles().setVisibleRowCount(3);

    }


    private void setCurrentUser(SetCurrentUserDTO dto) {
        empty(true);
        GetRoomsForUserResponseDTO response = connectionController.sendReceive(new GetRoomsForUserDTO(dto.getUser().getId()), GetRoomsForUserResponseDTO.class);
        if (response != null) {
            for (Long roomId : response.getRooms().keySet()) {
                RoomDto room = getRoom(roomId);
                roomAdded(room);
            }
        }
        panel.getBtnCreate().setEnabled(true);

        SwingUtilities.invokeLater(() -> {
            for (Long tileId : getTilesForUser(dto.getUser().getId())) {
                TileDto tile = getTile(tileId);
                tilesModel.addElement(tile);
            }
        });
    }


    private RoomDto getRoom(Long roomId) {
        GetRoomResultDTO response = connectionController.sendReceive(new GetRoomDTO(roomId), GetRoomResultDTO.class);
        return response.getRoom();
    }

    private TileDto getTile(Long tileId) {
        GetTileResultDTO response = connectionController.sendReceive(new GetTileDTO(tileId), GetTileResultDTO.class);
        return response.getTile();
    }

    private List<Long> getTilesForUser(Long userId) {
        GetTilesForUserResponseDTO response = connectionController.sendReceive(new GetTilesForUserDTO(userId), GetTilesForUserResponseDTO.class);
        return response.getTiles();
    }

    @Override
    public void connected() {
        empty(true);
        panel.getBtnCreate().setEnabled(true);
    }

    @Override
    public void disconnected() {
        empty(true);
        setEditable(false);
        panel.getBtnCreate().setEnabled(false);
    }


    private void empty(boolean thorough) {
        newRoom = false;
        tileImage = null;
        SwingUtilities.invokeLater(() -> {
            panel.getLblRoomID().setText("");
            panel.getTxtRoomName().setText("");
            panel.getLblImagePreview().setText("");
            panel.getLblImagePreview().setIcon(null);
            panel.getTxtWidth().setText("100");
            panel.getTxtLength().setText("100");
            panel.getTxtRows().setText("10");
            panel.getTxtCols().setText("10");
            panel.getBtnSave().setEnabled(false);
            panel.getBtnDelete().setEnabled(false);
            panel.getBtnChooseFile().setEnabled(false);
            if (thorough) {
                roomsModel.removeAllElements();
                panel.getBtnTeleport().setEnabled(false);
                tilesModel.removeAllElements();
            }
        });
    }

    private int findIndex(Long tileId) {
        int index = -1;
        for (int i = 0; i < tilesModel.getSize(); i++) {
            if (tilesModel.get(i).getId().equals(tileId)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void setEditable(boolean editable) {
        SwingUtilities.invokeLater(() -> {
            panel.getTxtRoomName().setEnabled(editable);
            panel.getTxtWidth().setEnabled(editable);
            panel.getTxtLength().setEnabled(editable);
            panel.getTxtRows().setEnabled(editable);
            panel.getTxtCols().setEnabled(editable);
            panel.getBtnChooseFile().setEnabled(editable);
        });
    }


    private void removeRoom(Long id) {
        RoomDetailsListItem found = null;
        for (int i = 0; i < roomsModel.getSize(); i++) {
            RoomDetailsListItem item = roomsModel.getElementAt(i);
            if (item != null && item.getRoomDto().getId().equals(id)) {
                found = item;
                break;
            }
        }
        if (found != null) {
            roomsModel.removeElement(found);
        }
    }

    public void roomAdded(RoomDto room) {
        SwingUtilities.invokeLater(() -> {
            removeRoom(room.getId());
            roomsModel.addElement(new RoomDetailsListItem(room));
        });
    }

    public void roomDeleted(Long roomId) {
        removeRoom(roomId);
    }

    public void roomUpdated(RoomDto room) {
        SwingUtilities.invokeLater(() -> {
            removeRoom(room.getId());
            roomsModel.addElement(new RoomDetailsListItem(room));
        });
    }


    private void tileSelected() {
        chosenTile = panel.getListTiles().getSelectedValue();
        tileImage = null;
        SwingUtilities.invokeLater(() -> {
            if (chosenTile != null) {
                Image image = ImageUtils.decode(chosenTile.getImageData());
                tileImage = (BufferedImage) image;
                Image iconImage = image.getScaledInstance(panel.getLblImagePreview().getWidth(), panel.getLblImagePreview().getHeight(), Image.SCALE_SMOOTH);
                panel.getLblImagePreview().setIcon(new ImageIcon(iconImage));
            } else {
                panel.getLblImagePreview().setIcon(null);
            }
        });
    }

    private void chooseFile() {
        int returnVal = fileChooser.showOpenDialog(panel);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                tileImage = ImageIO.read(file);
                chosenTile = null;
                SwingUtilities.invokeLater(() -> {
                    Image iconImage = tileImage.getScaledInstance(panel.getLblImagePreview().getWidth(), panel.getLblImagePreview().getHeight(), Image.SCALE_SMOOTH);
                    panel.getLblImagePreview().setIcon(new ImageIcon(iconImage));
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void teleport() {
        RoomDetailsListItem item = panel.getListRooms().getSelectedValue();
        //FIXME Teleport
    }

    private void deleteRoom() {
        Long roomId = Long.valueOf(panel.getLblRoomID().getText());
        DeleteRoomDTO deleteRoomDTO = new DeleteRoomDTO(roomId);
        connectionController.send(deleteRoomDTO);
        //FIXME Delete Room
        empty(false);
    }

    private void createRoom() {
        newRoom = true;
        chosenTile = null;
        SwingUtilities.invokeLater(() -> {
            panel.getLblRoomID().setText("");
            panel.getTxtRoomName().setText("New ROOM Name");
            panel.getBtnSave().setEnabled(true);
            panel.getListRooms().clearSelection();
            panel.getBtnDelete().setEnabled(false);
            tilesModel.removeAllElements();
            panel.getLblImagePreview().setIcon(null);
            tileImage = null;
            panel.getTxtRoomName().setEnabled(true);
            panel.getTxtWidth().setEnabled(false);
            panel.getTxtLength().setEnabled(false);
            panel.getTxtRows().setEnabled(false);
            panel.getTxtCols().setEnabled(false);
            panel.getBtnChooseFile().setEnabled(true);
        });
    }

    private void saveRoom() {
        String roomName = panel.getTxtRoomName().getText().trim();
        int width = Integer.valueOf(panel.getTxtWidth().getText());
        int length = Integer.valueOf(panel.getTxtLength().getText());
        int rows = Integer.valueOf(panel.getTxtRows().getText());
        int cols = Integer.valueOf(panel.getTxtCols().getText());
        Long tileID = null;
        if (chosenTile != null) {
            tileID = chosenTile.getId();
        }
        if (newRoom) {
            if (tileImage != null) {
                AddRoomDto addRoomDto = new AddRoomDto(roomName, width, length, rows, cols, null, ImageUtils.encode(tileImage));
                connectionController.send(addRoomDto);
            } else {
                AddRoomDto addRoomDto = new AddRoomDto(roomName, width, length, rows, cols, tileID, null);
                connectionController.send(addRoomDto);
            }
        } else {
            // Update room!
            Long roomId = Long.valueOf(panel.getLblRoomID().getText());
            if (tileImage != null) {
                UpdateRoomDto updateRoomDto = new UpdateRoomDto(roomId, roomName, width, length, rows, cols, null, ImageUtils.encode(tileImage));
                connectionController.send(updateRoomDto);
            } else {
                UpdateRoomDto updateRoomDto = new UpdateRoomDto(roomId, roomName, width, length, rows, cols, tileID, null);
                connectionController.send(updateRoomDto);
            }
        }


        setEditable(false);
        empty(false);
        panel.getListRooms().clearSelection();
        panel.getBtnDelete().setEnabled(false);
        panel.getBtnTeleport().setEnabled(false);
    }

    private void roomSelected(RoomDto room) {
        SwingUtilities.invokeLater(() -> {
            panel.getLblRoomID().setText(room.getId().toString());
            panel.getTxtRoomName().setText(room.getName());
            panel.getTxtLength().setText("" + room.getLength());
            panel.getTxtWidth().setText("" + room.getWidth());
            panel.getTxtCols().setText("" + room.getCols());
            panel.getTxtRows().setText("" + room.getRows());
            panel.getBtnSave().setEnabled(true);
            panel.getBtnDelete().setEnabled(true);
            panel.getBtnTeleport().setEnabled(true);
            panel.getListTiles().setEnabled(true);
            setEditable(true);
            tilesModel.removeAllElements();
            panel.getLblImagePreview().setIcon(null);

            if (room.getTileID() == null) {
                panel.getListTiles().clearSelection();
            } else {
                int index =  findIndex(room.getTileID());
                panel.getListTiles().setSelectedIndex(index);
            }
        });
    }

}
