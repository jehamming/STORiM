package com.hamming.storim.client.controller;

import com.hamming.storim.client.STORIMWindowController;
import com.hamming.storim.client.panels.LoginPanel;
import com.hamming.storim.common.MicroServerException;
import com.hamming.storim.common.MicroServerProxy;
import com.hamming.storim.common.ProtocolHandler;
import com.hamming.storim.common.StorimURI;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginWithTokenDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginWithTokenResultDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginResultDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LoginPanelController implements ConnectionListener {

    private LoginPanel panel;
    private STORIMWindowController windowController;
    private MicroServerProxy microServerProxy;


    public LoginPanelController(STORIMWindowController windowController, LoginPanel panel, MicroServerProxy microServerProxy) {
        this.panel = panel;
        this.microServerProxy = microServerProxy;
        microServerProxy.getConnectionController().addConnectionListener(this);
        this.windowController = windowController;
        setup();
    }

    public void doConnect() {
        windowController.setCurrentUser(null);
        windowController.setUserToken(null);

        String username = panel.getTxtUsername().getText().trim();
        String password = String.valueOf(panel.getTxtPassword().getPassword());
        String serverURLTxt = panel.getTxtServerURL().getText().trim();
        connectToServer(serverURLTxt, username, password);
    }

    public void connectToServer(String serverURLTxt, String username, String password) {
        try {
            StorimURI serverURI = new StorimURI(serverURLTxt);
            // Connect
            microServerProxy.connect("STORIM_Java_Client", serverURI.getServerip(), serverURI.getPort());
            // Do login request
            LoginResultDTO loginResult = microServerProxy.login(username, password, serverURI.getRoomId());
            windowController.setCurrentUser(loginResult.getUser());
            windowController.setUserToken(loginResult.getToken());
            panel.getBtnConnect().setEnabled(false);
        } catch (MicroServerException e) {
            JOptionPane.showMessageDialog(panel, e.getMessage());
            disconnect();
        }
    }


    public void connectToServer(Long userID, String token, String serverURItxt) {
        disconnect();
        try {
            StorimURI serverURI = new StorimURI(serverURItxt);
            // Connect
            microServerProxy.connect("STORIM_Java_Client", serverURI.getServerip(), serverURI.getPort());

            // Do connect request
            LoginWithTokenResultDTO loginWithTokenResultDTO = microServerProxy.loginWithToken(userID, token, serverURI.getRoomId());

            SwingUtilities.invokeLater(() -> {
                panel.getBtnConnect().setEnabled(false);
            });
        } catch (MicroServerException e) {
            JOptionPane.showMessageDialog(windowController.getWindow(), e.getMessage());
            disconnect();
        }
    }

    private void disconnect() {
        windowController.getFileMenuController().disconnect();
    }

    private void setup() {
        panel.getTxtServerURL().setText("storim://127.0.0.1:3334");
        panel.getTxtUsername().setText(windowController.getUsername());
        panel.getTxtPassword().setText(windowController.getPassword());
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

        panel.getBtnConnect().setEnabled(true);
        panel.getBtnConnect().addActionListener(e -> doConnect());
    }


    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {
        SwingUtilities.invokeLater(() -> {
            panel.getBtnConnect().setEnabled(true);
        });
    }
}
