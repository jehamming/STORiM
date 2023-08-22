package com.hamming.storim.client.controller.menu;

import com.hamming.storim.client.STORIMConnectionDetails;
import com.hamming.storim.client.STORIMRecentMenuItem;
import com.hamming.storim.client.STORIMWindow;
import com.hamming.storim.client.STORIMWindowController;
import com.hamming.storim.client.controller.LoginPanelController;
import com.hamming.storim.client.listitem.VerbListItem;
import com.hamming.storim.client.panels.ChatPanel;
import com.hamming.storim.client.panels.LoginPanel;
import com.hamming.storim.common.MicroServerProxy;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.protocol.request.ExecVerbDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginResultDTO;
import com.hamming.storim.common.dto.protocol.serverpush.*;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;
import com.hamming.storim.common.util.Logger;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileMenuController implements ConnectionListener {

    private LoginPanel loginPanel;
    private JFrame connectFrame;
    private LoginPanelController loginPanelController;
    private STORIMWindow window;
    private STORIMWindowController windowController;
    private List<STORIMConnectionDetails> recents;
    private static String RECENTS_FILENAME = "recents.dat";
    private MicroServerProxy microServerProxy;


    public FileMenuController(STORIMWindow storimWindow, STORIMWindowController windowController, MicroServerProxy microServerProxy) {
        this.window = storimWindow;
        this.windowController=  windowController;
        this.microServerProxy = microServerProxy;
        microServerProxy.getConnectionController().addConnectionListener(this);
        registerReceivers();
        setup();
    }

    private void registerReceivers() {
        microServerProxy.getConnectionController().registerReceiver(LoginResultDTO.class, (ProtocolReceiver<LoginResultDTO>) dto -> loginSuccess(dto.isSuccess()));
     }


    private void setup() {
        recents = new ArrayList<>();
        loadRecents();
        window.getMenuConnect().setEnabled(true);
        window.getMenuDisconnect().setEnabled(false);

        loginPanel = new LoginPanel();
        loginPanelController = new LoginPanelController(windowController, loginPanel, microServerProxy);
        window.getMenuConnect().addActionListener(e -> {
            connectFrame.setVisible(true);
        });
        //Prepare Frame
        connectFrame = new JFrame("Connect");
        connectFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        connectFrame.getContentPane().add(loginPanel);
        connectFrame.pack();
        connectFrame.setLocationRelativeTo(window);

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
            window.getMenuRecent().setEnabled(true);
        });
    }

    public LoginPanelController getLoginPanelController() {
        return loginPanelController;
    }

    private void loginSuccess(boolean success) {
        if ( success ) {
            storeConnectionDetails();
        }
        connectFrame.setVisible(!success);
        window.getMenuConnect().setEnabled(false);
        window.getMenuDisconnect().setEnabled(true);
        window.getMenuRecent().setEnabled(false);
    }

    private void storeConnectionDetails() {
        String url = loginPanel.getTxtServerURL().getText().trim();
        String username = loginPanel.getTxtUsername().getText().trim();
        String password = new String (loginPanel.getTxtPassword().getPassword());
        STORIMConnectionDetails details = findRecent(url +"-" + username);
        if ( details == null ) {
            details = new STORIMConnectionDetails(url, username, password);
            recents.add(details);
            STORIMRecentMenuItem newItem = new STORIMRecentMenuItem(details);
            newItem.addActionListener(e -> {
                loginPanelController.connectToServer(url, username,password );
            });
            window.getMenuRecent().add(newItem);
        } else {
            details.setPassword(password);
        }
    }


    private STORIMConnectionDetails findRecent(String name) {
        STORIMConnectionDetails found = null;
        for (STORIMConnectionDetails d : recents) {
            if ( d.toString().equals(name)) {
                found = d;
                break;
            }
        }
        return found;
    }

    public void storeRecents() {
        File file = new File(RECENTS_FILENAME);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(recents);
            oos.close();
            Logger.info(this, getClass().getSimpleName() + ": stored "+ recents.size() + " recents in "+ file.getAbsolutePath());
        } catch (IOException e) {
            Logger.info(this, ":" + "ERROR:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadRecents() {
        File file = new File(RECENTS_FILENAME);
        try {
            if ( file.exists() ) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                recents = (List<STORIMConnectionDetails>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            Logger.info(this, ":" + "ERROR:" + e.getMessage());
            e.printStackTrace();
        }

        for (STORIMConnectionDetails d : recents) {
            STORIMRecentMenuItem newItem = new STORIMRecentMenuItem(d);
            newItem.addActionListener(e -> {
                loginPanelController.connectToServer(d.getConnectURL(), d.getUsername(),d.getPassword() );
            });
            window.getMenuRecent().add(newItem);
        }
    }
}
