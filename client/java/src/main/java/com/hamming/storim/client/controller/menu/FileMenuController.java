package com.hamming.storim.client.controller.menu;

import com.hamming.storim.client.STORIMWindow;
import com.hamming.storim.client.STORIMWindowController;
import com.hamming.storim.client.controller.LoginPanelController;
import com.hamming.storim.client.listitem.VerbListItem;
import com.hamming.storim.client.panels.ChatPanel;
import com.hamming.storim.client.panels.LoginPanel;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.protocol.request.ExecVerbDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginResultDTO;
import com.hamming.storim.common.dto.protocol.serverpush.*;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class FileMenuController implements ConnectionListener {

    private ConnectionController connectionController;
    private LoginPanel loginPanel;
    private JFrame connectFrame;
    private LoginPanelController loginPanelController;
    private STORIMWindow window;
    private STORIMWindowController windowController;


    public FileMenuController(STORIMWindow storimWindow, STORIMWindowController windowController, ConnectionController connectionController) {
        this.window = storimWindow;
        this.windowController=  windowController;
        this.connectionController = connectionController;
        connectionController.addConnectionListener(this);
        registerReceivers();
        setup();
    }

    private void registerReceivers() {
       connectionController.registerReceiver(LoginResultDTO.class, (ProtocolReceiver<LoginResultDTO>) dto -> loginSuccess(dto.isSuccess()));
     }


    private void setup() {
        window.getMenuConnect().setEnabled(true);
        window.getMenuDisconnect().setEnabled(false);

        loginPanel = new LoginPanel();
        loginPanelController = new LoginPanelController(windowController, loginPanel, connectionController);
        window.getMenuConnect().addActionListener(e -> {
            connectFrame.setVisible(true);
        });
        //Prepare Frame
        connectFrame = new JFrame("Connect");
        connectFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        connectFrame.getContentPane().add(loginPanel);
        connectFrame.pack();

        window.getMenuDisconnect().addActionListener(e -> disconnect());

    }

    public void disconnect() {
        SwingUtilities.invokeLater(() -> {
            window.getMenuConnect().setEnabled(true);;
            window.getMenuDisconnect().setEnabled(false);;
        });
        windowController.setCurrentServerId(null);
    }

    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {
        SwingUtilities.invokeLater(() -> {
            window.getMenuConnect().setEnabled(true);
            window.getMenuDisconnect().setEnabled(false);
        });
    }

    public LoginPanelController getLoginPanelController() {
        return loginPanelController;
    }

    private void loginSuccess(boolean success) {
        connectFrame.setVisible(!success);
        window.getMenuConnect().setEnabled(false);
        window.getMenuDisconnect().setEnabled(true);
    }
}
