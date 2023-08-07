package com.hamming.storim.client.controller;

import com.hamming.storim.client.STORIMWindow;
import com.hamming.storim.client.listitem.ServerListItem;
import com.hamming.storim.client.panels.LoginPanel;
import com.hamming.storim.client.listitem.RoomListItem;
import com.hamming.storim.common.ProtocolHandler;
import com.hamming.storim.common.StorimURI;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.*;
import com.hamming.storim.common.interfaces.ConnectionListener;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URI;
import java.net.URL;

public class LoginPanelController implements ConnectionListener {

    private LoginPanel panel;
    private STORIMWindow storimWindow;
    private ConnectionController connectionController;


    public LoginPanelController(STORIMWindow storimWindow, LoginPanel panel, ConnectionController connectionController) {
        this.panel = panel;
        this.connectionController = connectionController;
        connectionController.addConnectionListener(this);
        this.storimWindow = storimWindow;
        setup();
    }

    public void disconnect() {
        connectionController.disconnect();
        SwingUtilities.invokeLater(() -> {
            panel.getBtnDisconnect().setEnabled(false);
            panel.getBtnConnect().setEnabled(true);;
        });
        storimWindow.setCurrentServerId(null);
    }

    public void doConnect() {
        storimWindow.setCurrentUser(null);
        storimWindow.setUserToken(null);

        String username = panel.getTxtUsername().getText().trim();
        String password = String.valueOf(panel.getTxtPassword().getPassword());

        String serverURLTxt = panel.getTxtServerURL().getText().trim();

        try {

            StorimURI serverURI = new StorimURI(serverURLTxt);

            // Connect
            connectionController.connect("STORIM_Java_Client", serverURI.getServerip(), serverURI.getPort());

            // Do login request
            LoginDTO dto = ProtocolHandler.getInstance().getLoginDTO(username, password, serverURI.getRoomId());
            LoginResultDTO loginResult = connectionController.sendReceive(dto, LoginResultDTO.class);

            if (loginResult.isLoginSucceeded()) {
                storimWindow.setCurrentUser(loginResult.getUser());
                storimWindow.setUserToken(loginResult.getToken());
                panel.getBtnConnect().setEnabled(false);
                panel.getBtnDisconnect().setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(panel, loginResult.getErrorMessage());
                disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel, e.getMessage());
        }
    }


    public void connectToServer(Long userID, String token, String serverURItxt) {
        disconnect();
        try {
            StorimURI serverURI = new StorimURI(serverURItxt);

            // Connect
            connectionController.connect("STORIM_Java_Client", serverURI.getServerip(), serverURI.getPort());

            // Do connect request
            ConnectDTO connectDTO = ProtocolHandler.getInstance().getConnectDTO(userID, token, serverURI.getRoomId());
            ConnectResultDTO connectResultDTO = connectionController.sendReceive(connectDTO, ConnectResultDTO.class);

            if (connectResultDTO.isConnectSucceeded()) {
                SwingUtilities.invokeLater(() -> {
                    panel.getBtnConnect().setEnabled(false);
                    panel.getBtnDisconnect().setEnabled(true);
                });
            } else {
                JOptionPane.showMessageDialog(panel, connectResultDTO.getErrorMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel, e);
        }
    }

    private void setup() {
        panel.getTxtServerURL().setText("storim://127.0.0.1:3334");
        panel.getTxtUsername().setText(storimWindow.getUsername());
        panel.getTxtPassword().setText(storimWindow.getPassword());
        panel.getTxtPassword().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doConnect();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        panel.getBtnDisconnect().setEnabled(false);
        panel.getBtnDisconnect().addActionListener(e -> disconnect());
        panel.add(panel.getBtnDisconnect());

        panel.getBtnConnect().setEnabled(true);
        panel.getBtnConnect().addActionListener(e -> doConnect());
    }


    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {
        SwingUtilities.invokeLater(() -> {
            panel.getBtnDisconnect().setEnabled(false);
            panel.getBtnConnect().setEnabled(true);
        });
    }
}
