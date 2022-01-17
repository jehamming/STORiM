package com.hamming.storim.client.panels;


import com.hamming.storim.client.ImageUtils;
import com.hamming.storim.common.Controllers;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.interfaces.RoomUpdateListener;
import com.hamming.storim.common.interfaces.UserListener;
import com.hamming.storim.common.dto.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author jehamming
 */
public class RoomEditorPanel extends javax.swing.JPanel  implements UserListener, ConnectionListener, RoomUpdateListener {

    private DefaultListModel<RoomListItem> roomsModel = new DefaultListModel<>();
    private DefaultListModel<TileDto> tilesModel = new DefaultListModel<>();
    private Controllers controllers;
    boolean newRoom = false;
    private JFileChooser fileChooser;
    private BufferedImage tileImage;
    private TileDto chosenTile;


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
        fileChooser = new JFileChooser();
        initComponents();
        setup();
        controllers.getRoomController().addRoomUpdateListener(this);
        controllers.getUserController().addUserListener(this);
        controllers.getConnectionController().addConnectionListener(this);
    }

    private void setup() {
        listTiles.setModel(tilesModel);
        listRooms.setModel(roomsModel);
        listRooms.addListSelectionListener(e -> {
            if ( !e.getValueIsAdjusting() ) { //Else this is called twice!
                RoomListItem item = listRooms.getSelectedValue();
                if (item != null && item.getRoom()!= null) {
                   roomSelected(item.getRoom());
                }
            }
        });
        btnDelete.addActionListener(e -> deleteRoom());
        btnSave.addActionListener(e -> saveRoom());
        setEditable(false);
        btnCreate.addActionListener(e -> createRoom());
        btnTeleport.addActionListener(e -> teleport());
        btnSave.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCreate.setEnabled(false);
        btnChooseFile.addActionListener(e -> chooseFile());
        listTiles.setCellRenderer( new TileRenderer() );
        listTiles.addListSelectionListener(e -> {
            tileSelected();
        });
        listTiles.setVisibleRowCount(3);

    }

    private void tileSelected() {
        chosenTile = (TileDto) listTiles.getSelectedValue();
        tileImage = null;
        SwingUtilities.invokeLater(() -> {
            if ( chosenTile != null ) {
                Image image = ImageUtils.decode(chosenTile.getImageData());
                tileImage = (BufferedImage) image;
                Image iconImage = image.getScaledInstance(lblImagePreview.getWidth(), lblImagePreview.getHeight(), Image.SCALE_SMOOTH);
                lblImagePreview.setIcon(new ImageIcon(iconImage));
            } else {
                lblImagePreview.setIcon(null);
            }
        });
    }

    private void chooseFile() {
        int returnVal = fileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                tileImage = ImageIO.read(file);
                chosenTile = null;
                SwingUtilities.invokeLater(() -> {
                    Image iconImage = tileImage.getScaledInstance(lblImagePreview.getWidth(), lblImagePreview.getHeight(), Image.SCALE_SMOOTH);
                    lblImagePreview.setIcon(new ImageIcon(iconImage));
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void teleport() {
        RoomListItem item = listRooms.getSelectedValue();
        if (item != null  && item.getRoom() != null ) {
            controllers.getRoomController().teleportRequest(controllers.getUserController().getCurrentUser().getId(), item.getRoom().getId());
        }
    }

    private void deleteRoom() {
        Long roomId = Long.valueOf(lblRoomID.getText());
        controllers.getRoomController().deleteRoom(roomId);
        empty(false);
    }

    private void createRoom() {
        setEnabled(true);
        newRoom = true;
        chosenTile = null;
        SwingUtilities.invokeLater(() -> {
            lblRoomID.setText("");
            txtRoomName.setText("New ROOM Name");
            btnSave.setEnabled(true);
            listRooms.clearSelection();
            btnDelete.setEnabled(false);
            tilesModel.removeAllElements();
            lblImagePreview.setIcon(null);
            tileImage = null;
            for (TileDto tile : controllers.getRoomController().getTiles() ) {
                tilesModel.addElement(tile);
            }
            txtRoomName.setEnabled(true);
            txtWidth.setEnabled(false);
            txtLength.setEnabled(false);
            txtRows.setEnabled(false);
            txtCols.setEnabled(false);
            btnChooseFile.setEnabled(true);
        });
    }

    private void saveRoom() {
        String roomName = txtRoomName.getText().trim();
        int width = 100, length = 100;
        int rows = 10, cols = 10;
        Long tileID = null;
        if (chosenTile != null) {
            tileID = chosenTile.getId();
        }
        if (newRoom) {
            if (tileImage != null ) {
                controllers.getRoomController().addRoom(roomName, tileID, ImageUtils.encode(tileImage));
            } else {
                controllers.getRoomController().addRoom(roomName, tileID, null);
            }
        } else {
            // Update room!
            Long roomId = Long.valueOf(lblRoomID.getText());

            if ( chosenTile != null ) {
                controllers.getRoomController().updateRoom(roomId, roomName, width, length, rows, cols, null, ImageUtils.encode(tileImage));
            } else {
                controllers.getRoomController().updateRoom(roomId, roomName, width, length, rows, cols, tileID, null);
            }


        }


        setEditable(false);
        empty(false);
        listRooms.clearSelection();
        btnDelete.setEnabled(false);
        btnTeleport.setEnabled(false);
    }

    private void roomSelected(RoomDto room) {
        SwingUtilities.invokeLater(() -> {
            lblRoomID.setText(room.getId().toString());
            txtRoomName.setText(room.getName());
            btnSave.setEnabled(true);
            btnDelete.setEnabled(true);
            btnTeleport.setEnabled(true);
            listTiles.setEnabled(true);
            setEditable(true);
            tilesModel.removeAllElements();
            lblImagePreview.setIcon(null);

            for (TileDto tile : controllers.getRoomController().getTiles() ) {
                tilesModel.addElement(tile);
            }

            if ( room.getTileID() == null ) {
                listTiles.clearSelection();
            } else {
                TileDto tile = controllers.getRoomController().getTile(room.getTileID());;
                int index =  findIndex(tile);
                listTiles.setSelectedIndex(index);
            }
        });

    }

    private int findIndex(TileDto tile) {
        int index = -1;
        for (int i = 0; i < tilesModel.getSize(); i++) {
            if (tilesModel.get(i).getId().equals( tile.getId() )) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void setEditable(boolean editable) {
        SwingUtilities.invokeLater(() -> {
            txtRoomName.setEnabled(editable);
            txtWidth.setEnabled(editable);
            txtLength.setEnabled(editable);
            txtRows.setEnabled(editable);
            txtCols.setEnabled(editable);
            btnChooseFile.setEnabled(editable);
        });
    }


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
        btnSave = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btnChooseFile = new javax.swing.JButton();
        lblImagePreview = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listTiles = new javax.swing.JList<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtWidth = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtLength = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtRows = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtCols = new javax.swing.JTextField();

        jLabel3.setText("Rooms");

        jScrollPane1.setViewportView(listRooms);

        btnTeleport.setText("Teleport");

        jLabel7.setText("Room ID");

        jLabel8.setText("Room Name");

        jLabel9.setText("Room Size ");

        btnCreate.setText("Create");

        btnDelete.setText("Delete");

        lblRoomID.setText("jLabel1");

        txtRoomName.setText("jTextField1");

        btnSave.setText("Save");

        jLabel2.setText("Room Tile");

        btnChooseFile.setText("Create new");

        lblImagePreview.setText("No Label Chosen");

        jScrollPane2.setViewportView(listTiles);

        jLabel4.setText("Choose below or :");

        jLabel5.setText("Width:");

        txtWidth.setText("100");

        jLabel1.setText("Length:");

        txtLength.setText("100");

        jLabel10.setText("Room Tiles ");

        jLabel6.setText("Rows:");

        txtRows.setText("10");

        jLabel11.setText("Cols");

        txtCols.setText("100");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel3)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                                                                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                        .addComponent(jLabel2)
                                                                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                .addComponent(lblImagePreview, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addComponent(lblRoomID, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(txtRoomName, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                                                                                        .addGroup(layout.createSequentialGroup()
                                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                        .addGroup(layout.createSequentialGroup()
                                                                                                                .addComponent(jLabel5)
                                                                                                                .addGap(3, 3, 3)
                                                                                                                .addComponent(txtWidth, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                        .addGroup(layout.createSequentialGroup()
                                                                                                                .addComponent(jLabel6)
                                                                                                                .addGap(8, 8, 8)
                                                                                                                .addComponent(txtRows, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                                                .addGap(4, 4, 4)
                                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                                                        .addComponent(jLabel11)
                                                                                                        .addComponent(jLabel1))))
                                                                                .addGap(3, 3, 3)
                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(txtLength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                        .addComponent(btnChooseFile, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                        .addComponent(txtCols, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGap(9, 9, 9)))))
                                                .addContainerGap(42, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnTeleport, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnCreate)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnDelete)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnSave)
                                                .addGap(111, 111, 111))))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel7)
                                                        .addComponent(lblRoomID))
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel8)
                                                        .addComponent(txtRoomName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel9)
                                                        .addComponent(jLabel5)
                                                        .addComponent(txtWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel1)
                                                        .addComponent(txtLength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel10)
                                                        .addComponent(jLabel6)
                                                        .addComponent(txtRows, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel11)
                                                        .addComponent(txtCols, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel2)
                                                        .addComponent(btnChooseFile)
                                                        .addComponent(jLabel4))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblImagePreview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnTeleport)
                                        .addComponent(btnCreate)
                                        .addComponent(btnDelete)
                                        .addComponent(btnSave))
                                .addGap(32, 32, 32))
        );
    }


    // Variables declaration - do not modify
    private javax.swing.JButton btnChooseFile;
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnTeleport;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblImagePreview;
    private javax.swing.JLabel lblRoomID;
    private javax.swing.JTextField txtCols;
    private javax.swing.JTextField txtLength;
    private javax.swing.JTextField txtRoomName;
    private javax.swing.JTextField txtRows;
    private javax.swing.JTextField txtWidth;
    private javax.swing.JList<RoomListItem> listRooms;
    private javax.swing.JList<TileDto> listTiles;



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

    @Override
    public void roomDeleted(RoomDto room) {
        removeRoom(room.getId());
    }



    @Override
    public void roomUpdated(RoomDto room) {
        if (controllers.getUserController().getCurrentUser()!= null && room.getOwnerID().equals(controllers.getUserController().getCurrentUser().getId())) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    removeRoom(room.getId());
                    roomsModel.addElement(new RoomListItem(room.getName(), room))   ;
                }
            });
        }
    }

    public void empty(boolean thorough) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                lblRoomID.setText("");
                txtRoomName.setText("");
                lblImagePreview.setText("");
                lblImagePreview.setIcon(null);
                tileImage = null;
                txtWidth.setText("100");
                txtLength.setText("100");
                txtRows.setText("10");
                txtCols.setText("10");
                btnSave.setEnabled(false);
                btnDelete.setEnabled(false);
                btnChooseFile.setEnabled(false);
                if (thorough) {
                    roomsModel.removeAllElements();
                    btnTeleport.setEnabled(false);
                    tilesModel.removeAllElements();
                }
            }
        });
        newRoom = false;
    }


    @Override
    public void userConnected(UserDto user) {
        if ((controllers.getUserController().getCurrentUser().getId().equals( user.getId() ))) {
            empty(true);
            for (RoomDto room: controllers.getRoomController().getRoomsForUser(controllers.getUserController().getCurrentUser().getId())) {
                roomAdded(room);
            }
            btnCreate.setEnabled(true);
        }

    }

    @Override
    public void userUpdated(UserDto user) {

    }

    @Override
    public void userDisconnected(UserDto user) {

    }

    @Override
    public void userOnline(UserDto user) {

    }

    @Override
    public void loginResult(boolean success, String message) {

    }

    @Override
    public void userTeleported(Long userId, LocationDto location) {
    }

    @Override
    public void avatarAdded(AvatarDto avatar) {

    }

    @Override
    public void avatarDeleted(AvatarDto avatar) {

    }

    @Override
    public void avatarUpdated(AvatarDto avatar) {

    }


    @Override
    public void connected() {
        empty(true);
        btnCreate.setEnabled(true);
    }

    @Override
    public void disconnected() {
        setEditable(false);
        btnCreate.setEnabled(false);
    }


    // End of variables declaration




}