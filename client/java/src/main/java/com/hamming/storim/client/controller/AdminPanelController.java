package com.hamming.storim.client.controller;

import com.hamming.storim.client.ImageUtils;
import com.hamming.storim.client.STORIMWindow;
import com.hamming.storim.client.controller.admin.AdminUserController;
import com.hamming.storim.client.listitem.ThingListItem;
import com.hamming.storim.client.listitem.UserListItem;
import com.hamming.storim.client.listitem.VerbListItem;
import com.hamming.storim.client.panels.AdminPanel;
import com.hamming.storim.common.ProtocolHandler;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.request.AddThingDto;
import com.hamming.storim.common.dto.protocol.request.DeleteThingDTO;
import com.hamming.storim.common.dto.protocol.request.PlaceThingInRoomDTO;
import com.hamming.storim.common.dto.protocol.request.UpdateThingDto;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginResultDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.VerifyAdminRequestDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.VerifyAdminResponseDTO;
import com.hamming.storim.common.dto.protocol.serverpush.SetCurrentUserDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class AdminPanelController implements ConnectionListener {

    private ConnectionController connectionController;
    private AdminPanel panel;
    private STORIMWindow storimWindow;

    private UserDto currentUser;

    private boolean isAdmin;

    private AdminUserController adminUserController;



    public AdminPanelController(STORIMWindow storimWindow, AdminPanel panel, ConnectionController connectionController) {
        this.panel = panel;
        this.storimWindow = storimWindow;
        this.connectionController = connectionController;
        adminUserController = new AdminUserController(this, panel);
        connectionController.addConnectionListener(this);
        registerReceivers();
        setup();
        empty(true);
        isAdmin = false;
    }


    private void registerReceivers() {
        connectionController.registerReceiver(SetCurrentUserDTO.class, (ProtocolReceiver<SetCurrentUserDTO>) dto -> setCurrentUser(dto));
    }

    private void setCurrentUser(SetCurrentUserDTO dto) {
        currentUser = dto.getUser();
    }


    private void setup() {
        panel.getBtnEnableAdmin().addActionListener(e -> enableAdmin());
        panel.getTxtAdminpassword().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enableAdmin();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        adminUserController.setup();

        enableTabs(false);

    }

    private void enableAdmin() {
        if ( !connectionController.isConnected()) return;

        String adminpassWord = panel.getTxtAdminpassword().getText().trim();
        if ( adminpassWord != null && !adminpassWord.equals("")) {
            VerifyAdminRequestDTO requestDTO = ProtocolHandler.getInstance().getVerifyAdminRequestDTO(adminpassWord);
            VerifyAdminResponseDTO response = connectionController.sendReceive(requestDTO, VerifyAdminResponseDTO.class);

            if (response.isSuccess()) {
                isAdmin = true;
                enableTabs(true);
                panel.getBtnEnableAdmin().setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(panel, response.getErrorMessage());
            }
        }
    }

    private void enableTabs(boolean enabled) {
        panel.getTabPaneAdmin().setVisible(enabled);
        panel.getTabPaneAdmin().setEnabled(enabled);
    }


    public boolean isAdmin() {
        return isAdmin;
    }

    public void empty(boolean fully) {
        adminUserController.empty(fully);
    }



    @Override
    public void connected() {
        empty(true);
        isAdmin = false;
        panel.getBtnEnableAdmin().setEnabled(true);
        enableTabs(false);
        panel.getBtnEnableAdmin().setEnabled(true);
    }

    @Override
    public void disconnected() {
        currentUser = null;
        isAdmin = false;
        panel.getBtnEnableAdmin().setEnabled(true);
        enableTabs(false);
    }
    public UserDto getCurrentUser() {
        return currentUser;
    }


    public ConnectionController getConnectionController() {
        return connectionController;
    }
}
