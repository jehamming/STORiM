package com.hamming.storim.client.panels;


import javax.swing.*;

/**
 * @author jehamming
 */
public class ExitEditorPanel extends JPanel{
//
//
//    private DefaultListModel<ExitListItem> exitsModel = new DefaultListModel<>();
//    private DefaultComboBoxModel<RoomListItem> roomsModel = new DefaultComboBoxModel<>();
//    private DefaultComboBoxModel<ServerListItem> serversModel = new DefaultComboBoxModel<>();
//    private JList<ExitListItem> listExitsInCurrentRoom;
//    boolean newExit = false;
//
//    @Override
//    public void setRoom(RoomDto room, LocationDto location) {
//        empty(true);
//        updateServerList();
//        SwingUtilities.invokeLater(() -> {
//            for (Long exitId : room.getExits()) {
//                ExitDto exit = controllers.getRoomController().getExit(exitId);
//                ExitListItem elItem = new ExitListItem(exit.getName(), exit);
//                exitsModel.addElement(elItem);
//            }
//            btnCreate.setEnabled(true);
//        });
//
//    }
//
//    private void updateServerList() {
//        //FIXME update exits
////        GetServerRegistrationsResponseDTO serverRegistrationsResponseDTO =
////        List<ServerRegistrationDTO> servers = controllers.getConnectionController().sendReceive(new GetServerRegistrationsRequestDTO());
////        SwingUtilities.invokeLater(()->{
////            serversModel.removeAllElements();
////            if (servers != null ) {
////                for (ServerRegistrationDTO server : servers) {
////                    ServerListItem item = new ServerListItem(server.getServerName());
////                    serversModel.addElement(item);
////                }
////            }
////        });
//    }
//
//    private class ExitListItem {
//        private String name;
//        private ExitDto exit;
//        public ExitListItem(String name, ExitDto exit) {
//            this.name = name;
//            this.exit = exit;
//        }
//
//        public ExitDto getExit() {
//            return exit;
//        }
//
//        @Override
//        public String toString() {
//            return name;
//        }
//    }
//
//    private class RoomListItem {
//        private RoomDto room;
//        public RoomListItem(RoomDto room) {
//            this.room = room;
//        }
//        public RoomDto getRoom() {
//            return room;
//        }
//        @Override
//        public String toString() {
//            return "(" + room.getId() +") " + room.getName();
//        }
//    }
//
//    private class ServerListItem {
//        private String serverName;
//        public ServerListItem(String serverName) {
//            this.serverName = serverName;
//        }
//
//        public String getServerName() {
//            return serverName;
//        }
//
//        @Override
//        public String toString() {
//            return serverName;
//        }
//    }
//
//    public ExitEditorPanel(Controllers controllers) {
//        this.controllers = controllers;
//        initComponents();
//        setup();
//        controllers.getRoomController().addRoomListener(this);
//    }
//
//    private void setup() {
//        listExitsInCurrentRoom.setModel(exitsModel);
//        listExitsInCurrentRoom.addListSelectionListener(e -> {
//            if (!e.getValueIsAdjusting()) { //Else this is called twice!
//                ExitListItem item = listExitsInCurrentRoom.getSelectedValue();
//                if (item != null && item.getExit() != null) {
//                    exitSelected(item.getExit());
//                }
//            }
//        });
//        cmbToRooms.setModel(roomsModel);
//        cmbToServer.setModel(serversModel);
//        btnDelete.addActionListener(e -> deleteExit());
//        btnSave.addActionListener(e -> saveExit());
//        setEditable(false);
//        btnCreate.addActionListener(e -> createExit());
//        btnSave.setEnabled(false);
//        btnDelete.setEnabled(false);
//        btnCreate.setEnabled(false);
//    }
//
//    private void createExit() {
//        empty(false);
//        newExit = true;
//        setEditable(true);
//    }
//
//    private void saveExit() {
//        //FIXME  Implement
//    }
//
//    private void deleteExit() {
//        //FIXME  Implement
//    }
//
//    private void exitSelected(ExitDto exit) {
//        SwingUtilities.invokeLater(() -> {
//            lblExitID.setText(exit.getId().toString());
//            txtExitName.setText(exit.getName());
//            cmbOrientation.getModel().setSelectedItem(exit.getOrientation().toString());
//            setEditable(true);
//        });
//    }
//
//
//    private void setEditable(boolean editable) {
//        SwingUtilities.invokeLater(() -> {
//            txtExitName.setEnabled(editable);
//            cmbOrientation.setEnabled(editable);
//            cmbToRooms.setEnabled(editable);
//            cmbToServer.setEnabled(editable);
//        });
//    }
//
//
//    private void initComponents() {
//        jLabel3 = new javax.swing.JLabel();
//        jScrollPane1 = new javax.swing.JScrollPane();
//        listExitsInCurrentRoom = new javax.swing.JList<>();
//        jLabel7 = new javax.swing.JLabel();
//        jLabel8 = new javax.swing.JLabel();
//        jLabel9 = new javax.swing.JLabel();
//        btnCreate = new javax.swing.JButton();
//        btnDelete = new javax.swing.JButton();
//        lblExitID = new javax.swing.JLabel();
//        txtExitName = new javax.swing.JTextField();
//        btnSave = new javax.swing.JButton();
//        jLabel10 = new javax.swing.JLabel();
//        cmbToRooms = new JComboBox<RoomListItem>();
//        cmbOrientation = new javax.swing.JComboBox<>();
//        jLabel11 = new javax.swing.JLabel();
//        cmbToServer = new JComboBox<ServerListItem>();
//
//        jLabel3.setText("Current Exits");
//
//        jScrollPane1.setViewportView(listExitsInCurrentRoom);
//
//        jLabel7.setText("Exit ID");
//
//        jLabel8.setText("Exit Name");
//
//        jLabel9.setText("to Room");
//
//        btnCreate.setText("New");
//
//        btnDelete.setText("Delete");
//
//        lblExitID.setText("jLabel1");
//
//        txtExitName.setText("jTextField1");
//
//        btnSave.setText("Save");
//
//        jLabel10.setText("Orientation");
//
//        cmbOrientation.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"NORTH", "SOUTH", "EAST", "WEST"}));
//
//        jLabel11.setText("to Server");
//
//        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
//        this.setLayout(layout);
//        layout.setHorizontalGroup(
//                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                        .addGroup(layout.createSequentialGroup()
//                                .addContainerGap()
//                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                        .addGroup(layout.createSequentialGroup()
//                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                                        .addGroup(layout.createSequentialGroup()
//                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
//                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
//                                                        .addGroup(layout.createSequentialGroup()
//                                                                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
//                                                        .addGroup(layout.createSequentialGroup()
//                                                                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                                                .addGap(54, 54, 54)))
//                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
//                                                        .addComponent(txtExitName, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
//                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
//                                                                .addComponent(cmbOrientation, 0, 271, Short.MAX_VALUE)
//                                                                .addComponent(cmbToRooms, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                                                .addComponent(cmbToServer, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                                                .addComponent(lblExitID, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))))
//                                        .addGroup(layout.createSequentialGroup()
//                                                .addComponent(jLabel3)
//                                                .addGap(0, 541, Short.MAX_VALUE))
//                                        .addGroup(layout.createSequentialGroup()
//                                                .addGap(167, 167, 167)
//                                                .addComponent(btnCreate)
//                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                                .addComponent(btnDelete)
//                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                                .addComponent(btnSave)))
//                                .addContainerGap())
//        );
//        layout.setVerticalGroup(
//                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                        .addGroup(layout.createSequentialGroup()
//                                .addContainerGap()
//                                .addComponent(jLabel3)
//                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                        .addGroup(layout.createSequentialGroup()
//                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
//                                                        .addComponent(jLabel7)
//                                                        .addComponent(lblExitID))
//                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
//                                                        .addComponent(jLabel8)
//                                                        .addComponent(txtExitName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
//                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
//                                                        .addComponent(cmbToServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                                        .addGroup(layout.createSequentialGroup()
//                                                                .addGap(6, 6, 6)
//                                                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
//                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
//                                                        .addComponent(cmbToRooms, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                                        .addGroup(layout.createSequentialGroup()
//                                                                .addGap(6, 6, 6)
//                                                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
//                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
//                                                        .addComponent(cmbOrientation)
//                                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
//                                                .addGap(0, 0, Short.MAX_VALUE))
//                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE))
//                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
//                                        .addComponent(btnCreate)
//                                        .addComponent(btnDelete)
//                                        .addComponent(btnSave))
//                                .addGap(32, 32, 32))
//        );
//    }
//
//    private javax.swing.JButton btnCreate;
//    private javax.swing.JButton btnDelete;
//    private javax.swing.JButton btnSave;
//    private javax.swing.JComboBox<String> cmbOrientation;
//    private JComboBox<RoomListItem> cmbToRooms;
//    private JComboBox<ServerListItem> cmbToServer;
//    private javax.swing.JLabel jLabel10;
//    private javax.swing.JLabel jLabel11;
//    private javax.swing.JLabel jLabel3;
//    private javax.swing.JLabel jLabel7;
//    private javax.swing.JLabel jLabel8;
//    private javax.swing.JLabel jLabel9;
//    private javax.swing.JScrollPane jScrollPane1;
//    private javax.swing.JLabel lblExitID;
//    private javax.swing.JTextField txtExitName;
//
//
//
//    public void empty(boolean thorough) {
//        SwingUtilities.invokeLater(() -> {
//            lblExitID.setText("");
//            txtExitName.setText("");
//            btnSave.setEnabled(false);
//            btnDelete.setEnabled(false);
//            if (thorough) exitsModel.removeAllElements();
//        });
//        newExit = false;
//    }
//
//
//    @Override
//    public void thingPlacedInRoom(ThingDto thing, UserDto byUser) {
//
//    }
//
//    @Override
//    public void thingRemovedFromRoom(ThingDto thing) {
//
//    }
//
//    @Override
//    public void thingInRoom(ThingDto thing) {
//
//    }
//
//    @Override
//    public void userInRoom(UserDto user, LocationDto location) {
//
//    }
//
//    @Override
//    public void userEnteredRoom(UserDto user, LocationDto location) {
//
//    }
//
//    @Override
//    public void userLeftRoom(UserDto user) {
//
//    }
//
//    @Override
//    public void userLocationUpdate(UserDto user, LocationDto location) {
//
//    }
//
//    @Override
//    public void currentUserLocationUpdate(Long sequenceNumber, LocationDto location) {
//
//    }
//
//    // End of variables declaration
//

}