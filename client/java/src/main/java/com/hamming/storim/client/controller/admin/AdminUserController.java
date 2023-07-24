package com.hamming.storim.client.controller.admin;

import com.hamming.storim.client.controller.AdminPanelController;
import com.hamming.storim.client.listitem.ShortUserListItem;
import com.hamming.storim.client.panels.AdminPanel;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.protocol.requestresponse.GetUsersRequestDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetUsersResultDTO;

import javax.swing.*;

public class AdminUserController {
    private AdminPanelController adminPanelController;
    private ConnectionController connectionController;
    private AdminPanel panel;
    private DefaultListModel<ShortUserListItem> usersModel = new DefaultListModel<>();
    private boolean newUser;

    public AdminUserController(AdminPanelController adminPanelController, AdminPanel panel) {
        this.adminPanelController = adminPanelController;
        this.connectionController = adminPanelController.getConnectionController();
        this.panel = panel;
    }


    public void setup() {
        //Users
        panel.getBtnGetUsers().addActionListener(e -> getUsers());
        panel.getListUsers().setModel(usersModel);
        panel.getListUsers().addListSelectionListener(e -> {
            ShortUserListItem item = panel.getListUsers().getSelectedValue();
            if (item != null && item.getUserId() != null) {
                userSelected(item.getUserId());
            }
        });
        panel.getBtnDeleteUser().addActionListener(e -> deleteUser());
        panel.getBtnNewUser().addActionListener(e -> addNewUser());
        panel.getBtnSaveUser().addActionListener(e -> saveUser());
        panel.getBtnSetPassword().addActionListener(e -> setUserpassword());

        panel.getBtnNewUser().setEnabled(true);
        panel.getBtnSaveUser().setEnabled(false);
        panel.getBtnSetPassword().setEnabled(false);
        panel.getBtnDeleteUser().setEnabled(false);
    }

    private void getUsers() {
        GetUsersResultDTO response = connectionController.sendReceive(new GetUsersRequestDTO(), GetUsersResultDTO.class);
        if (response != null) {
            for (Long userId :response.getUsers().keySet()) {
               String userName = response.getUsers().get(userId);
               ShortUserListItem item = new ShortUserListItem(userId, userName);
                usersModel.addElement(item);
            }
        }
    }

    private void setUserpassword() {
    }

    private void saveUser() {
    }

    private void addNewUser() {
    }

    private void deleteUser() {
    }

    private void userSelected(Long userId) {
    }


    public void setEditable(boolean editable) {
        SwingUtilities.invokeLater(() -> {
            panel.getTxtEmail().setEditable(editable);
            panel.getTxtEmail().setEditable(editable);
            panel.getTxtPassword().setEditable(editable);
        });
    }

    public void empty(boolean fully) {
        SwingUtilities.invokeLater(() -> {
            panel.getLblUserID().setText("");
            panel.getTxtUserDisplayname().setText("");
            panel.getTxtUsername().setText("");
            panel.getTxtPassword().setText("");
            panel.getBtnNewUser().setEnabled(true);
            panel.getBtnSaveUser().setEnabled(false);
            panel.getBtnSetPassword().setEnabled(false);
            panel.getBtnDeleteUser().setEnabled(false);

            if (fully) {
                panel.getListUsers().clearSelection();
                usersModel.removeAllElements();
                panel.getListUsers().removeAll();
            }
        });
        newUser = false;
    }
}
