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
import com.hamming.storim.common.util.Logger;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LoginPanelController implements ConnectionListener {

    private LoginPanel panel;
    private STORIMWindowController windowController;
    private MicroServerProxy microServerProxy;
    private StorimURI currentServerURI;


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
        currentServerURI = new StorimURI(serverURLTxt);
        connectToServer(serverURLTxt, username, password);
    }

    public void connectToServer(String serverURLTxt, String username, String password) {
        try {
            currentServerURI = new StorimURI(serverURLTxt);
            // Connect
            microServerProxy.connect(currentServerURI);
            // Do login request
            LoginResultDTO loginResult = microServerProxy.login(username, password, currentServerURI.getRoomId());
            windowController.setCurrentUser(loginResult.getUser());
            windowController.setUserToken(loginResult.getToken());
            panel.getBtnConnect().setEnabled(false);
        } catch (MicroServerException e) {
            JOptionPane.showMessageDialog(panel, e.getMessage());
            currentServerURI = null;
        }
    }


    public void connectToServer(Long userID, String token, String serverURItxt) {
        boolean revert = false;
        StorimURI old = null;
        try {
            // Check if already connected
            if ( microServerProxy.isConnected()) {
                revert = true;
                old = currentServerURI;
            }
            StorimURI serverURI = new StorimURI(serverURItxt);
            // Connect
            microServerProxy.connect(serverURI);
            // Store URI
            currentServerURI = serverURI;
            // Do connect using Token request
            microServerProxy.loginWithToken(userID, token, serverURI.getRoomId());
            SwingUtilities.invokeLater(() -> {
                panel.getBtnConnect().setEnabled(false);
            });
        } catch (MicroServerException e) {
            JOptionPane.showMessageDialog(windowController.getWindow(), "Error Connecting to '"+ serverURItxt+"', redirecting to previous server");
            Logger.error(this, e.getMessage());
            e.printStackTrace();
            if ( revert && old != null ) {
                Logger.info(this, "connection to server failed, reverting to " + old);
                connectToServer(userID, token, old.getServerURL());
            }
        }
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
