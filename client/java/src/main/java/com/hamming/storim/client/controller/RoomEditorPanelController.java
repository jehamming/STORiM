package com.hamming.storim.client.controller;

import com.hamming.storim.client.STORIMWindowController;
import com.hamming.storim.client.listitem.AvatarListItem;
import com.hamming.storim.client.listitem.RoomDetailsListItem;
import com.hamming.storim.client.panels.RoomEditorPanel;
import com.hamming.storim.common.MicroServerException;
import com.hamming.storim.common.MicroServerProxy;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.request.AddRoomDto;
import com.hamming.storim.common.dto.protocol.request.DeleteRoomDTO;
import com.hamming.storim.common.dto.protocol.request.TeleportRequestDTO;
import com.hamming.storim.common.dto.protocol.request.UpdateRoomDto;
import com.hamming.storim.common.dto.protocol.requestresponse.*;
import com.hamming.storim.common.dto.protocol.serverpush.*;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;

public class RoomEditorPanelController implements ConnectionListener {

    private RoomEditorPanel panel;
    private STORIMWindowController windowController;
    private DefaultListModel<AvatarListItem> avatarModel = new DefaultListModel<>();
    private DefaultListModel<RoomDetailsListItem> roomsModel = new DefaultListModel<>();
    private DefaultListModel<TileDto> tilesModel = new DefaultListModel<>();
    boolean newRoom = false;
    private UserDto currentUser;
    private RoomDto selectedRoom;
    private MicroServerProxy microServerProxy;


    public RoomEditorPanelController(STORIMWindowController windowController, RoomEditorPanel panel, MicroServerProxy microServerProxy) {
        this.panel = panel;
        this.windowController = windowController;
        this.microServerProxy = microServerProxy;
        microServerProxy.getConnectionController().addConnectionListener(this);
        registerReceivers();
        setup();
    }


    private void registerReceivers() {
        microServerProxy.getConnectionController().registerReceiver(SetCurrentUserDTO.class, (ProtocolReceiver<SetCurrentUserDTO>) dto -> setCurrentUser(dto));
        microServerProxy.getConnectionController().registerReceiver(RoomAddedDTO.class, (ProtocolReceiver<RoomAddedDTO>) dto -> roomAdded(dto.getRoom()));
        microServerProxy.getConnectionController().registerReceiver(RoomUpdatedDTO.class, (ProtocolReceiver<RoomUpdatedDTO>) dto -> roomUpdated(dto.getRoom()));
        microServerProxy.getConnectionController().registerReceiver(RoomDeletedDTO.class, (ProtocolReceiver<RoomDeletedDTO>) dto -> roomDeleted(dto.getRoomId()));
        microServerProxy.getConnectionController().registerReceiver(TileAddedDTO.class, (ProtocolReceiver<TileAddedDTO>) dto -> tileAdded(dto.getTile()));
    }

    private void tileAdded(TileDto tile) {
        SwingUtilities.invokeLater(() -> {
            tilesModel.addElement(tile);
        });
    }

    private void setup() {
        selectedRoom = null;
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
        panel.getBtnEditAuthorisation().addActionListener(e -> {
            AuthorisationPanelController.showAuthorisationPanel(panel, selectedRoom, microServerProxy);
        });
        panel.getBtnEditAuthorisation().setEnabled(false);
    }


    private void setCurrentUser(SetCurrentUserDTO dto) {
        empty(true);
        currentUser = dto.getUser();
        try {
            HashMap<Long, String> rooms = microServerProxy.getRoomsForUser(currentUser.getId());
            for (Long roomId : rooms.keySet()) {
                RoomDto room = getRoom(roomId);
                roomAdded(room);
            }
            panel.getBtnCreate().setEnabled(true);
        } catch (MicroServerException e) {
            throw new RuntimeException(e);
        }
    }


    private RoomDto getRoom(Long roomId) {
        return microServerProxy.getRoom(roomId);
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
        selectedRoom = null;
        SwingUtilities.invokeLater(() -> {
            panel.getLblId().setText("");
            panel.getTfRoomName().setText("");
            panel.getTxtRows().setText("10");
            panel.getTxtCols().setText("10");
            panel.getBtnSave().setEnabled(false);
            panel.getBtnDelete().setEnabled(false);
            panel.getBtnEditAuthorisation().setEnabled(false);
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
            panel.getTfRoomName().setEnabled(editable);
            panel.getTxtRows().setEnabled(editable);
            panel.getTxtCols().setEnabled(editable);
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
        // check if not currently selected
        try {
            Long selectedRoomId = Long.valueOf(panel.getLblId().getText());
            if (roomId == selectedRoomId) {
                empty(false);
            }
        } catch ( NumberFormatException e ) {
            // do nothing,
        }
    }

    public void roomUpdated(RoomDto room) {
        SwingUtilities.invokeLater(() -> {
            removeRoom(room.getId());
            roomsModel.addElement(new RoomDetailsListItem(room));
        });
    }

    private void teleport() {
        RoomDetailsListItem item = panel.getListRooms().getSelectedValue();
        Long roomId = item.getRoomDto().getId();
        microServerProxy.teleport(currentUser.getId(), roomId);
    }

    private void deleteRoom() {
        Long roomId = Long.valueOf(panel.getLblId().getText());
        microServerProxy.deleteRoom(roomId);
        empty(false);
    }

    private void createRoom() {
        newRoom = true;
        SwingUtilities.invokeLater(() -> {
            panel.getLblId().setText("");
            panel.getTfRoomName().setText("New ROOM Name");
            panel.getBtnSave().setEnabled(true);
            panel.getListRooms().clearSelection();
            panel.getBtnDelete().setEnabled(false);
            panel.getTfRoomName().setEnabled(true);
            panel.getTxtRows().setEnabled(true);
            panel.getTxtCols().setEnabled(true);
            panel.getBtnEditAuthorisation().setEnabled(false);
            panel.getBtnCreate().setEnabled(false);
        });
    }

    private void saveRoom() {
        String roomName = panel.getTfRoomName().getText().trim();
        int rows = Integer.valueOf(panel.getTxtRows().getText());
        int cols = Integer.valueOf(panel.getTxtCols().getText());

        if (newRoom) {
            microServerProxy.addRoom(roomName, rows, cols);
        } else {
            // Update room!
            Long roomId = Long.valueOf(panel.getLblId().getText());
            microServerProxy.updateRoom(roomId, roomName, rows, cols, null, null, null, null);
        }

        setEditable(false);
        empty(false);
        panel.getListRooms().clearSelection();
        panel.getBtnDelete().setEnabled(false);
        panel.getBtnTeleport().setEnabled(false);
        panel.getBtnCreate().setEnabled(true);
    }

    private void roomSelected(RoomDto room) {
        selectedRoom = room;

        SwingUtilities.invokeLater(() -> {
            panel.getLblId().setText(room.getId().toString());
            panel.getTfRoomName().setText(room.getName());
            panel.getTxtCols().setText("" + room.getCols());
            panel.getTxtRows().setText("" + room.getRows());
            panel.getBtnSave().setEnabled(true);
            panel.getBtnDelete().setEnabled(true);
            panel.getBtnTeleport().setEnabled(true);
            panel.getBtnEditAuthorisation().setEnabled(true);
            setEditable(true);
        });
    }

}
