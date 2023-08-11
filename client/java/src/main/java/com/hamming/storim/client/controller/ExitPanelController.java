package com.hamming.storim.client.controller;

import com.hamming.storim.client.ImageUtils;
import com.hamming.storim.client.STORIMWindowController;
import com.hamming.storim.client.STORIMWindowOld;
import com.hamming.storim.client.listitem.ExitListItem;
import com.hamming.storim.client.listitem.RoomListItem;
import com.hamming.storim.client.panels.ExitPanel;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.*;
import com.hamming.storim.common.dto.protocol.request.*;
import com.hamming.storim.common.dto.protocol.requestresponse.*;
import com.hamming.storim.common.dto.protocol.serverpush.*;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ExitPanelController implements ConnectionListener {

    private ConnectionController connectionController;
    private ExitPanel panel;
    private STORIMWindowController windowController;

    private JFileChooser fileChooser;
    private BufferedImage tileImage;
    private DefaultListModel<ExitListItem> exitsModel = new DefaultListModel<>();
    private DefaultComboBoxModel<RoomListItem> roomsModel = new DefaultComboBoxModel<>();
    private boolean newExit = false;
    private Image exitImage;
    private UserDto currentUser;
    private RoomDto currentRoom;
    private Image defaultExitImage;

    public ExitPanelController(STORIMWindowController windowController, ExitPanel panel, ConnectionController connectionController) {
        this.panel = panel;
        this.windowController = windowController;
        this.connectionController = connectionController;
        this.fileChooser = new JFileChooser();
        connectionController.addConnectionListener(this);
        registerReceivers();
        setup();
        empty(true);

    }


    private void registerReceivers() {
        connectionController.registerReceiver(SetCurrentUserDTO.class, (ProtocolReceiver<SetCurrentUserDTO>) dto -> setCurrentUser(dto));
        connectionController.registerReceiver(SetRoomDTO.class, (ProtocolReceiver<SetRoomDTO>) dto -> setRoom(dto.getRoom()));
        connectionController.registerReceiver(ExitAddedDTO.class, (ProtocolReceiver<ExitAddedDTO>) dto -> exitAdded(dto.getExitDto()));
        connectionController.registerReceiver(ExitDeletedDTO.class, (ProtocolReceiver<ExitDeletedDTO>) dto -> exitDeleted(dto.getExitID()));
        connectionController.registerReceiver(ExitUpdatedDTO.class, (ProtocolReceiver<ExitUpdatedDTO>) dto -> exitUpdated(dto.getExitDto()));
        connectionController.registerReceiver(ExitInRoomDTO.class, (ProtocolReceiver<ExitInRoomDTO>) dto -> exitAdded(dto.getExitDto()));
        connectionController.registerReceiver(RoomAddedDTO.class, (ProtocolReceiver<RoomAddedDTO>) dto -> roomAdded(dto.getRoom()));
    }

    private void roomAdded(RoomDto room) {
        SwingUtilities.invokeLater(() -> {
            String roomName = room.getName();
            RoomListItem rli = new RoomListItem(room.getId(), roomName);
            SwingUtilities.invokeLater(() -> {
                roomsModel.addElement(rli);
            });
        });


    }

    private void setRoom(RoomDto room) {
        currentRoom = room;
        SwingUtilities.invokeLater(() -> {
            panel.getListExits().clearSelection();
            exitsModel.removeAllElements();
            roomsModel.removeAllElements();
            panel.getListExits().removeAll();
        });
        fillRoomsCombobox();
    }

    private void setCurrentUser(SetCurrentUserDTO dto) {
        currentUser = dto.getUser();
    }

    private void setup() {
        try {
            defaultExitImage = ImageIO.read(new File("resources/Exit.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        panel.getListExits().setModel(exitsModel);
        panel.getCmbRoom().setModel(roomsModel);
        panel.getBtnDelete().addActionListener(e -> deleteExit());
        panel.getBtnSave().addActionListener(e -> saveExit());
        panel.getBtnCreate().addActionListener(e -> createExit());
        panel.getBtnChange().addActionListener(e -> chooseFile());
        panel.getListExits().addListSelectionListener(e -> {
            ExitListItem item = panel.getListExits().getSelectedValue();
            if (item != null && item.getExitDto() != null) {
                exitSelected(item.getExitDto());
            }
        });
        setEditable(false);
        panel.getBtnSave().setEnabled(false);
        panel.getBtnDelete().setEnabled(false);
        panel.getBtnCreate().setEnabled(false);
        panel.getCmbRoom().addActionListener(e -> roomSelected());
    }

    private void fillRoomsCombobox() {
        GetRoomsDTO getRoomsDTO = new GetRoomsDTO();
        GetRoomsResultDTO getRoomsResultDTO = connectionController.sendReceive(getRoomsDTO, GetRoomsResultDTO.class);
        if ( getRoomsResultDTO != null && getRoomsResultDTO.getRooms() != null) {
            HashMap<Long, String> rooms = getRoomsResultDTO.getRooms();

            for (Long roomId : rooms.keySet() ) {
                if ( currentRoom.getId() != roomId ) {
                    String roomName = rooms.get(roomId);
                    RoomListItem rli = new RoomListItem(roomId, roomName);
                    SwingUtilities.invokeLater(() -> {
                        roomsModel.addElement(rli);
                    });
                }
            }
        }
    }

    private void roomSelected() {
        RoomListItem roomListItem = (RoomListItem) roomsModel.getSelectedItem();
        if (roomListItem != null) {
            String txt = "to " + roomListItem.getRoomName();
            panel.getTxtName().setText(txt);
        }
    }


    private void exitSelected(ExitDto exit) {
        SwingUtilities.invokeLater(() -> {
            RoomListItem roomListItem = getRoomListItem(exit.getToRoomId());
            panel.getCmbRoom().setSelectedItem(roomListItem);
            panel.getLblID().setText(exit.getId().toString());
            panel.getTxtName().setText(exit.getName());
            panel.getTaDescription().setText(exit.getDescription());
            panel.getSlScale().setValue((int) (exit.getScale() * 100));
            panel.getSlRotation().setValue(exit.getRotation());
            panel.getBtnSave().setEnabled(true);
            panel.getBtnDelete().setEnabled(true);
            setEditable(true);
            exitImage = ImageUtils.decode(exit.getImageData());
            Image iconImage = exitImage.getScaledInstance(panel.getLblImagePreview().getWidth(), panel.getLblImagePreview().getHeight(), Image.SCALE_SMOOTH);
            panel.getLblImagePreview().setIcon(new ImageIcon(iconImage));
        });
    }

    private RoomListItem getRoomListItem(Long roomID) {
        RoomListItem found = null;
        for (int i = 0; i < roomsModel.getSize(); i++) {
            if ( roomsModel.getElementAt(i).getId().equals(roomID)) {
                found = roomsModel.getElementAt(i);
                break;
            }
        }
        return found;
    }

    private void chooseFile() {
        int returnVal = fileChooser.showOpenDialog(panel);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                exitImage = ImageIO.read(file);
                SwingUtilities.invokeLater(() -> {
                    Image iconImage = exitImage.getScaledInstance(panel.getLblImagePreview().getWidth(), panel.getLblImagePreview().getHeight(), Image.SCALE_SMOOTH);
                    panel.getLblImagePreview().setIcon(new ImageIcon(iconImage));
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createExit() {
        panel.setEnabled(true);
        newExit = true;
        SwingUtilities.invokeLater(() -> {
            exitImage = defaultExitImage;
            Image iconImage = exitImage.getScaledInstance(panel.getLblImagePreview().getWidth(), panel.getLblImagePreview().getHeight(), Image.SCALE_SMOOTH);
            panel.getLblImagePreview().setIcon(new ImageIcon(iconImage));
            panel.getLblID().setText("");
            panel.getTxtName().setText("New Exit name");
            panel.getTaDescription().setText("New Exit Description");
            panel.getSlScale().setValue(100);
            panel.getSlRotation().setValue(0);
            panel.getBtnSave().setEnabled(true);
            panel.getListExits().clearSelection();
            panel.getBtnDelete().setEnabled(false);
            panel.getTfRoomURI().setText("");
            setEditable(true);
        });
    }

    private void saveExit() {
        String exitName = panel.getTxtName().getText().trim();
        String exitDescription = panel.getTaDescription().getText();
        Float exitScale = (float) panel.getSlScale().getValue() / 100;
        Integer exitRotation = panel.getSlRotation().getValue();
        Long toRoomID = null ;
        if ( roomsModel.getSelectedItem() != null ) {
              toRoomID =((RoomListItem)roomsModel.getSelectedItem()).getId();
        }
        String toRoomURI = panel.getTfRoomURI().getText().trim();
        byte[] imageData = ImageUtils.encode(exitImage);
        if (newExit) {
            AddExitDto addExitDto = new AddExitDto(exitName, toRoomURI ,toRoomID, exitDescription, exitScale, exitRotation, imageData);
            connectionController.send(addExitDto);
            setEditable(false);
            empty(false);
            panel.getListExits().clearSelection();
            panel.getBtnDelete().setEnabled(false);
        } else {
            // Update !
            Long thingId = Long.valueOf(panel.getLblID().getText());
            UpdateExitDto updateExitDto = new UpdateExitDto(thingId, exitName, toRoomID, toRoomURI, exitDescription, exitScale, exitRotation, ImageUtils.encode(exitImage));
            connectionController.send(updateExitDto);
        }

    }

    private void deleteExit() {
        Long thingID = Long.valueOf(panel.getLblID().getText());
        DeleteExitDTO deleteExitDTO = new DeleteExitDTO(thingID);
        connectionController.send(deleteExitDTO);
        empty(false);
    }

    private void setEditable(boolean editable) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                panel.getTxtName().setEnabled(editable);
                panel.getTaDescription().setEnabled(editable);
                panel.getBtnChange().setEnabled(editable);
                panel.getSlRotation().setEnabled(editable);
                panel.getSlScale().setEnabled(editable);
            }
        });
    }

    public void empty(boolean fully) {
        SwingUtilities.invokeLater(() -> {
            panel.getLblID().setText("");
            panel.getTxtName().setText("");
            panel.getTaDescription().setText("");
            panel.getLblImagePreview().setText("");
            panel.getLblImagePreview().setIcon(null);
            panel.getBtnSave().setEnabled(false);
            panel.getBtnDelete().setEnabled(false);
            panel.getBtnChange().setEnabled(false);
            if (fully) {
                panel.getListExits().clearSelection();
                exitsModel.removeAllElements();
                roomsModel.removeAllElements();
                panel.getListExits().removeAll();
            }
        });
        newExit = false;
    }

    public void exitAdded(ExitDto exit) {
        SwingUtilities.invokeLater(() -> {
            exitsModel.addElement(new ExitListItem(exit));
        });
    }

    public void exitDeleted(Long exitID) {
        SwingUtilities.invokeLater(() -> {
            ExitListItem found = null;
            for (int i = 0; i < exitsModel.getSize(); i++) {
                ExitListItem item = exitsModel.get(i);
                if (item.getExitDto().getId().equals(exitID)) {
                    found = item;
                    break;
                }
            }
            if (found != null) {
                exitsModel.removeElement(found);
            }
        });
    }
    public void exitDeleted(ExitDto exit) {
        SwingUtilities.invokeLater(() -> {
            ExitListItem found = null;
            for (int i = 0; i < exitsModel.getSize(); i++) {
                ExitListItem item = exitsModel.get(i);
                if (item.getExitDto().getId().equals(exit.getId())) {
                    found = item;
                    break;
                }
            }
            if (found != null) {
                exitsModel.removeElement(found);
            }
        });
    }

    public void exitUpdated(ExitDto exit) {
        SwingUtilities.invokeLater(() -> {
            ExitListItem found = null;
            for (int i = 0; i < exitsModel.getSize(); i++) {
                ExitListItem item = exitsModel.get(i);
                if (item.getExitDto().getId().equals(exit.getId())) {
                    item.setExitDto(exit);
                    break;
                }
            }
            panel.getListExits().invalidate();
            panel.getListExits().repaint();
        });
    }

    private ExitListItem getItem(ExitDto exit) {
        ExitListItem found = null;
        for (int i = 0; i < exitsModel.getSize(); i++) {
            ExitListItem item = exitsModel.get(i);
            if (item.getExitDto().getId().equals(exit.getId())) {
                found = item;
                break;
            }
        }
        return found;
    }

    public void exitSelectedInView(ExitDto exit) {
        SwingUtilities.invokeLater(() -> {
            ExitListItem item = getItem(exit);
            if (item != null) {
                panel.getListExits().setSelectedValue(item, true);
            }
        });
    }


    @Override
    public void connected() {
        empty(true);
        panel.getBtnCreate().setEnabled(true);
    }

    @Override
    public void disconnected() {
        currentRoom = null;
        currentUser = null;
    }
}
