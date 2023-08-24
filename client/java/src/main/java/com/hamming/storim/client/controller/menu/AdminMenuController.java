package com.hamming.storim.client.controller.menu;

import com.hamming.storim.client.STORIMWindow;
import com.hamming.storim.client.STORIMWindowController;
import com.hamming.storim.client.controller.admin.AdminUserController;
import com.hamming.storim.client.controller.admin.ServerConfigurationController;
import com.hamming.storim.client.panels.AdminUsersPanel;
import com.hamming.storim.client.panels.ServerConfigurationPanel;
import com.hamming.storim.common.MicroServerProxy;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginResultDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginWithTokenResultDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;

import javax.swing.*;

public class AdminMenuController implements ConnectionListener {

    private AdminUsersPanel adminUsersPanel;
    private ServerConfigurationPanel serverConfigurationPanel;
    private JFrame adminEditUsersFrame;
    private JFrame adminEditServerConfigurationFrame;
    private AdminUserController adminUserController;
    private ServerConfigurationController serverConfigurationController;
    private STORIMWindow window;
    private STORIMWindowController windowController;

    private JMenuItem editServerConfigurationMenu;
    private MicroServerProxy microServerProxy;

    public AdminMenuController(STORIMWindow storimWindow, STORIMWindowController windowController, MicroServerProxy microServerProxy) {
        this.window = storimWindow;
        this.windowController=  windowController;
        this.microServerProxy = microServerProxy;
        microServerProxy.getConnectionController().addConnectionListener(this);
        registerReceivers();
        setup();
    }

    private void registerReceivers() {
       microServerProxy.getConnectionController().registerReceiver(LoginResultDTO.class, (ProtocolReceiver<LoginResultDTO>) dto -> loginResult(dto));
       microServerProxy.getConnectionController().registerReceiver(LoginWithTokenResultDTO.class, (ProtocolReceiver<LoginWithTokenResultDTO>) dto -> loginWithTokenResult(dto));
     }


    private void setup() {
        setUpEditUsers();
        setupEditServerConfiguration();
    }

    private void setupEditServerConfiguration() {
        editServerConfigurationMenu = new JMenuItem("Edit Server configuration");
        window.getMenuAdmin().add(editServerConfigurationMenu);
        editServerConfigurationMenu.setEnabled(false);

        serverConfigurationPanel = new ServerConfigurationPanel();
        serverConfigurationController = new ServerConfigurationController(serverConfigurationPanel, microServerProxy);

        editServerConfigurationMenu.addActionListener(e -> {
            adminEditServerConfigurationFrame.setVisible(true);
        });
        //Prepare Frame
        adminEditServerConfigurationFrame = new JFrame("Edit Server Configuration");
        adminEditServerConfigurationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adminEditServerConfigurationFrame.getContentPane().add(serverConfigurationPanel);
        adminEditServerConfigurationFrame.pack();
        adminEditServerConfigurationFrame.setLocationRelativeTo(window);
    }

    private void setUpEditUsers() {
        window.getMenuAdminEditUsers().setEnabled(false);

        adminUsersPanel = new AdminUsersPanel();
        adminUserController = new AdminUserController(adminUsersPanel, microServerProxy);

        window.getMenuAdminEditUsers().addActionListener(e -> {
            adminEditUsersFrame.setVisible(true);
        });
        //Prepare Frame
        adminEditUsersFrame = new JFrame("Edit Users");
        adminEditUsersFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adminEditUsersFrame.getContentPane().add(adminUsersPanel);
        adminEditUsersFrame.pack();
        adminEditUsersFrame.setLocationRelativeTo(window);
    }

    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {
        SwingUtilities.invokeLater(() -> {
            window.getMenuAdminEditUsers().setEnabled(false);
            editServerConfigurationMenu.setEnabled(false);
        });
    }

    private void loginResult(LoginResultDTO dto) {
        if ( dto.isSuccess() && dto.isServerAdmin() ) {
            editServerConfigurationMenu.setEnabled(true);
        }
        if ( dto.isSuccess() && dto.isUserdataServerAdmin() ) {
            window.getMenuAdminEditUsers().setEnabled(true);
        }
    }

    private void loginWithTokenResult(LoginWithTokenResultDTO dto) {
        if ( dto.isSuccess() && dto.isServerAdmin() ) {
            editServerConfigurationMenu.setEnabled(true);
        }
        if ( dto.isSuccess() && dto.isUserdataServerAdmin() ) {
            window.getMenuAdminEditUsers().setEnabled(true);
        }
    }

}
