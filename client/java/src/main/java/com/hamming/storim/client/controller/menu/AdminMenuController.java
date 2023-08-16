package com.hamming.storim.client.controller.menu;

import com.hamming.storim.client.STORIMWindow;
import com.hamming.storim.client.STORIMWindowController;
import com.hamming.storim.client.controller.AdminPanelController;
import com.hamming.storim.client.controller.LoginPanelController;
import com.hamming.storim.client.panels.AdminPanel;
import com.hamming.storim.client.panels.LoginPanel;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginResultDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;

import javax.swing.*;

public class AdminMenuController implements ConnectionListener {

    private ConnectionController connectionController;
    private AdminPanel adminPanel;
    private JFrame adminFrame;
    private AdminPanelController adminPanelController;
    private STORIMWindow window;
    private STORIMWindowController windowController;


    public AdminMenuController(STORIMWindow storimWindow, STORIMWindowController windowController, ConnectionController connectionController) {
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
        window.getMenuAdminPassword().setEnabled(false);
        window.getMenuAdminEditUsers().setEnabled(false);

        adminPanel = new AdminPanel();
        adminPanelController = new AdminPanelController(windowController, adminPanel, connectionController);

        window.getMenuAdminEditUsers().addActionListener(e -> {
            adminFrame.setVisible(true);
        });
        //Prepare Frame
        adminFrame = new JFrame("Admin");
        adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adminFrame.getContentPane().add(adminPanel);
        adminFrame.pack();


    }

    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {
        SwingUtilities.invokeLater(() -> {
            window.getMenuAdminPassword().setEnabled(false);
            window.getMenuAdminEditUsers().setEnabled(false);
        });
    }

    private void loginSuccess(boolean success) {
        window.getMenuAdminEditUsers().setEnabled(true);
    }
}
