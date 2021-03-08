package nl.hamming.storimapp.panels;


import com.hamming.storim.Controllers;
import com.hamming.storim.interfaces.ConnectionListener;
import com.hamming.storim.interfaces.RoomListener;
import com.hamming.storim.interfaces.UserListener;
import com.hamming.storim.model.dto.LocationDto;
import com.hamming.storim.model.dto.RoomDto;
import com.hamming.storim.model.dto.UserDto;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author jehamming
 */
public class RoomEditorPanel extends javax.swing.JPanel  implements RoomListener, UserListener, ConnectionListener {

    private DefaultListModel<RoomListItem> roomsModel = new DefaultListModel<RoomListItem>();
    private Controllers controllers;
    boolean newRoom = false;



    private class RoomListItem {
        private String name;
        private RoomDto room;
        public RoomListItem(String name, RoomDto room) {
            this.name = name;
            this.room = room;
        }
        public RoomDto getRoom() {
            return room;
        }
        @Override
        public String toString() {
            return name;
        }
    }
    /**
     * Creates new form UserInfoPanel
     * @param controllers
     */
    public RoomEditorPanel(Controllers controllers) {
        this.controllers = controllers;
        initComponents();
        setup();
        controllers.getRoomController().addRoomListener(this);
        controllers.getUserController().addUserListener(this);
        controllers.getConnectionController().addConnectionListener(this);
    }

    private void setup() {
        listRooms.setModel(roomsModel);
        listRooms.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if ( !e.getValueIsAdjusting() ) { //Else this is called twice!
                    RoomListItem item = listRooms.getSelectedValue();
                    if (item != null && item.getRoom()!= null) {
                        roomSelected(item.getRoom());
                    }
                }
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRoom();
            }
        });
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveRoom();
            }
        });
        setEditable(false);
        btnCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createRoom();
            }
        });
        btnTeleport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teleport();
            }
        });
        btnSave.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCreate.setEnabled(false);
        SpinnerModel value = new SpinnerNumberModel(10, 10, 50, 1);
        spinSize.setModel(value);
    }

    private void teleport() {
        RoomListItem item = listRooms.getSelectedValue();
        if (item != null  && item.getRoom() != null ) {
            controllers.getMoveController().teleport(controllers.getUserController().getCurrentUser(), item.getRoom());
        }
    }

    private void deleteRoom() {
        Long roomId = Long.valueOf(lblRoomID.getText());
        controllers.getRoomController().deleteRoom(roomId);
        empty(false);
    }

    private void createRoom() {
        setEnabled(true);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                lblRoomID.setText("");
                txtRoomName.setText("New ROOM Name");
                btnSave.setEnabled(true);
                listRooms.clearSelection();
                btnDelete.setEnabled(false);
                setEditable(true);
            }
        });
        newRoom = true;
    }

    private void saveRoom() {
        String roomName = txtRoomName.getText().trim();
        int roomSize = Integer.valueOf((Integer) spinSize.getModel().getValue());
        if (newRoom) {
            controllers.getRoomController().addRoom(roomName, roomSize);
        } else {
            // Update room!
            Long roomId = Long.valueOf(lblRoomID.getText());
            controllers.getRoomController().updateRoom(roomId, roomName, roomSize);
        }


        setEditable(false);
        empty(false);
        listRooms.clearSelection();
        btnDelete.setEnabled(false);
        btnTeleport.setEnabled(false);
    }

    private void roomSelected(RoomDto room) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                lblRoomID.setText(room.getId().toString());
                txtRoomName.setText(room.getName());
                spinSize.setValue(room.getSize());
                btnSave.setEnabled(true);
                btnDelete.setEnabled(true);
                btnTeleport.setEnabled(true);
                setEditable(true);
            }
        });
    }

    private void setEditable(boolean editable) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                txtRoomName.setEnabled(editable);
                spinSize.setEnabled(editable);
            }
        });
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listRooms = new javax.swing.JList<>();
        btnTeleport = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        btnCreate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        lblRoomID = new javax.swing.JLabel();
        txtRoomName = new javax.swing.JTextField();
        spinSize = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();

        jLabel3.setText("Rooms");

        jScrollPane1.setViewportView(listRooms);

        btnTeleport.setText("Teleport");

        jLabel7.setText("Room ID");

        jLabel8.setText("Room Name");

        jLabel9.setText("Room Size");

        btnCreate.setText("Create");

        btnDelete.setText("Delete");

        lblRoomID.setText("jLabel1");

        txtRoomName.setText("jTextField1");

        jLabel1.setText("Number of tiles (square)");

        btnSave.setText("Save");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(btnTeleport, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(btnCreate)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(btnDelete)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnSave))
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                                                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(lblRoomID, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(txtRoomName, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGroup(layout.createSequentialGroup()
                                                                        .addComponent(spinSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                                .addContainerGap(26, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel7)
                                                        .addComponent(lblRoomID))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel8)
                                                        .addComponent(txtRoomName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel9)
                                                        .addComponent(spinSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel1))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnTeleport)
                                        .addComponent(btnCreate)
                                        .addComponent(btnDelete)
                                        .addComponent(btnSave))
                                .addContainerGap(24, Short.MAX_VALUE))
        );
    }// </editor-fold>


    // Variables declaration - do not modify
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnTeleport;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblRoomID;
    private javax.swing.JList<RoomListItem> listRooms;
    private javax.swing.JSpinner spinSize;
    private javax.swing.JTextField txtRoomName;

    @Override
    public void userInRoom(UserDto user, RoomDto room, LocationDto location) {

    }

    @Override
    public void userTeleportedInRoom(UserDto user, RoomDto room) {

    }

    @Override
    public void userLeftRoom(UserDto user, RoomDto room) {

    }

    @Override
    public void roomAdded(RoomDto room) {
        if (controllers.getUserController().getCurrentUser()!= null && room.getOwnerID().equals(controllers.getUserController().getCurrentUser().getId())) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    removeRoom(room.getId());
                    roomsModel.addElement(new RoomListItem(room.getName(), room));
                }
            });
        }
    }

    private void removeRoom(Long id) {
        RoomListItem found = null;
        for (int i = 0; i < roomsModel.getSize(); i++) {
            RoomListItem item = (RoomListItem) roomsModel.getElementAt(i);
            if (item != null && item.getRoom().getId().equals(id)) {
                found = item;
                break;
            }
        }
        if (found != null) {
            roomsModel.removeElement(found);
        }
    }

    @Override
    public void roomDeleted(RoomDto room) {
        removeRoom(room.getId());
    }

    public void empty(boolean thorough) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                lblRoomID.setText("");
                txtRoomName.setText("");
                spinSize.setValue(20);
                btnSave.setEnabled(false);
                btnDelete.setEnabled(false);
                if (thorough) {
                    roomsModel.removeAllElements();
                    btnTeleport.setEnabled(false);
                }
            }
        });
        newRoom = false;
    }


    @Override
    public void userConnected(UserDto user) {

    }

    @Override
    public void userDisconnected(UserDto user) {

    }

    @Override
    public void userOnline(UserDto user) {

    }

    @Override
    public void loginResult(boolean success, String message) {
        empty(true);
        if (success) {
            for (RoomDto room: controllers.getRoomController().getRoomsForUser(controllers.getUserController().getCurrentUser().getId())) {
                roomAdded(room);
            }
        }
        btnCreate.setEnabled(true);
    }

    @Override
    public void currentUserLocation(LocationDto loc) {

    }

    @Override
    public void userLocationUpdate(Long userId, LocationDto loc) {

    }

    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {
        setEditable(false);
        btnCreate.setEnabled(false);
    }


    // End of variables declaration
}