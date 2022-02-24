package com.hamming.storim.client.controller;

import com.hamming.storim.client.STORIMWindow;
import com.hamming.storim.client.listitem.ServerListItem;
import com.hamming.storim.client.panels.LoginPanel;
import com.hamming.storim.client.listitem.RoomListItem;
import com.hamming.storim.common.ProtocolHandler;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.ServerRegistrationDTO;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.*;
import com.hamming.storim.common.interfaces.ConnectionListener;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class LoginPanelController implements ConnectionListener {

    private LoginPanel panel;
    private STORIMWindow storimWindow;
    private ConnectionController connectionController;
    private DefaultComboBoxModel<RoomListItem> roomsModel = new DefaultComboBoxModel<>();
    private DefaultComboBoxModel<ServerListItem> serversModel = new DefaultComboBoxModel<>();


    public LoginPanelController(STORIMWindow storimWindow, LoginPanel panel, ConnectionController connectionController) {
        this.panel = panel;
        this.connectionController = connectionController;
        connectionController.addConnectionListener(this);
        this.storimWindow = storimWindow;
        setup();
    }

    private void disconnect() {
        connectionController.disconnect();
        SwingUtilities.invokeLater(() -> {
            panel.getBtnDisconnect().setEnabled(false);
            panel.getBtnLogin().setEnabled(true);
            panel.getBtnConnectToServer().setEnabled(false);
            serversModel.removeAllElements();
            roomsModel.removeAllElements();
        });
    }

    public void doLogin() {
        storimWindow.setCurrentUser(null);
        storimWindow.setUserToken(null);
        String server = panel.getTxtIP().getText().trim();
        String strPort = panel.getTxtPort().getText().trim();
        Integer port = Integer.valueOf(strPort);
        String username = panel.getTxtUsername().getText().trim();
        String password = String.valueOf(panel.getTxtPassword().getPassword());
        try {
            // Connect
            connectionController.connect("STORIM_Java_Client", server, port);

            // Do login request
            LoginDTO dto = ProtocolHandler.getInstance().getLoginDTO(username, password);
            LoginResultDTO loginResult =  connectionController.sendReceive(dto,LoginResultDTO.class);

            if (loginResult.isLoginSucceeded()) {
                storimWindow.setCurrentUser(loginResult.getUser());
                storimWindow.setUserToken(loginResult.getToken());
                panel.getBtnDisconnect().setEnabled(true);
                panel.getBtnLogin().setEnabled(false);
                if (loginResult.getLocation() == null) {
                    updateServerList();
                }
            } else {
                JOptionPane.showMessageDialog(panel, loginResult.getErrorMessage());
            }


        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel, e.getMessage());
        }
    }

    private void updateServerList() {
        GetServerRegistrationsResponseDTO getServerRegistrationsResponseDTO = connectionController.sendReceive(new GetServerRegistrationsDTO(), GetServerRegistrationsResponseDTO.class);
        if (getServerRegistrationsResponseDTO != null) {
            List<ServerRegistrationDTO> servers = getServerRegistrationsResponseDTO.getServers();
            SwingUtilities.invokeLater(() -> {
                serversModel.removeAllElements();
                if (servers != null) {
                    for (ServerRegistrationDTO server : servers) {
                        ServerListItem item = new ServerListItem(server);
                        serversModel.addElement(item);
                    }
                }
            });
        }
    }

    private void serverSelected() {
        ServerListItem serverListItem = (ServerListItem) serversModel.getSelectedItem();
        if (serverListItem != null) {
            SwingUtilities.invokeLater(() -> {
                roomsModel.removeAllElements();
                String serverName = serverListItem.getServerRegistrationDTO().getServerName();
                GetRoomsForServerResponseDTO getRoomsForServerResponseDTO = connectionController.sendReceive(new GetRoomsForServerDTO(serverName), GetRoomsForServerResponseDTO.class);
                if (getRoomsForServerResponseDTO != null && getRoomsForServerResponseDTO.getRooms() != null) {
                    for (Long id : getRoomsForServerResponseDTO.getRooms().keySet()) {
                        String roomName = getRoomsForServerResponseDTO.getRooms().get(id);
                        RoomListItem roomListItem = new RoomListItem(id, roomName);
                        roomsModel.addElement(roomListItem);
                    }
                }
            });
        }
    }

    private void connectToServer() {
        ServerListItem serverListItem = (ServerListItem) serversModel.getSelectedItem();
        RoomListItem roomListItem = (RoomListItem) roomsModel.getSelectedItem();
        if (serverListItem != null && roomListItem != null) {
            try {
                connectionController.disconnect();
                String serverName = serverListItem.getServerRegistrationDTO().getServerURL();
                int serverPort = serverListItem.getServerRegistrationDTO().getServerPort();
                connectionController.connect("STORIM_Java_Client", serverName, serverPort);
                UserDto currentUser = storimWindow.getCurrentUser();
                String userToken = storimWindow.getUserToken();
                ConnectDTO connectRequestDTO = new ConnectDTO(currentUser.getId(), userToken, roomListItem.getId());
                connectionController.send(connectRequestDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setup() {
        panel.getCmbRoom().setModel(roomsModel);
        panel.getCmbServer().setModel(serversModel);
        panel.getTxtIP().setText("127.0.0.1");
        panel.getTxtPort().setText("3401");
        panel.getTxtUsername().setText("jehamming");
        panel.getTxtPassword().setText("jehamming");
        panel.getTxtPassword().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doLogin();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        panel.getBtnDisconnect().setEnabled(false);
        panel.getBtnDisconnect().addActionListener(e -> disconnect());
        panel.add(panel.getBtnDisconnect());

        panel.getBtnLogin().setEnabled(true);
        panel.getBtnLogin().addActionListener(e -> doLogin());

        panel.getBtnConnectToServer().setEnabled(false);
        panel.getBtnConnectToServer().addActionListener(e -> connectToServer());

        panel.getCmbServer().addActionListener(e -> serverSelected());
        panel.getCmbRoom().addActionListener(e -> roomSelected());
    }

    private void roomSelected() {
        panel.getBtnConnectToServer().setEnabled(true);
    }


    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {
//        SwingUtilities.invokeLater(() -> {
//            panel.getBtnDisconnect().setEnabled(false);
//            panel.getBtnLogin().setEnabled(true);
//            panel.getBtnConnectToServer().setEnabled(false);
//            serversModel.removeAllElements();
//            roomsModel.removeAllElements();
//        });
    }
}
