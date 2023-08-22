package com.hamming.storim.client.controller.admin;

import com.hamming.storim.client.listitem.ShortUserListItem;
import com.hamming.storim.client.panels.AdminUsersPanel;
import com.hamming.storim.common.MicroServerException;
import com.hamming.storim.common.MicroServerProxy;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.util.StringUtils;

import javax.swing.*;
import java.util.HashMap;

public class AdminUserController {
    private AdminUsersPanel panel;
    private DefaultListModel<ShortUserListItem> usersModel = new DefaultListModel<>();
    private boolean newUser;
    private MicroServerProxy microServerProxy;

    public AdminUserController(AdminUsersPanel panel, MicroServerProxy microServerProxy) {
        this.microServerProxy = microServerProxy;
        this.panel = panel;
        setup();
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

        panel.getBtnNewUser().setEnabled(true);
        panel.getBtnSaveUser().setEnabled(false);
        panel.getBtnDeleteUser().setEnabled(false);
    }

    private void getUsers() {
        SwingUtilities.invokeLater(() -> {
            usersModel.removeAllElements();
            try {
                HashMap<Long, String> users = microServerProxy.getUsers();
                for (Long userId : users.keySet()) {
                    String userName = users.get(userId);
                    ShortUserListItem item = new ShortUserListItem(userId, userName);
                    usersModel.addElement(item);
                }
            } catch (MicroServerException e) {
                JOptionPane.showMessageDialog(panel, e.getMessage());
            }
        });
    }

    private void saveUser() {
        if (newUser) {
            String username = panel.getTxtUsername().getText().trim();
            String name = panel.getTxtUserDisplayname().getText().trim();
            String email = panel.getTxtEmail().getText().trim();
            String passwordRaw = panel.getTxtPassword().getText().trim();
            String password = StringUtils.hashPassword(passwordRaw);
            try {
                microServerProxy.addUser(username, password, name, email);
                newUser = false;
                empty(false);
            } catch (MicroServerException e) {
                JOptionPane.showMessageDialog(panel, e.getMessage());
            }
        } else {
            // Update user
            Long id = Long.parseLong(panel.getLblUserID().getText());
            String username = panel.getTxtUsername().getText().trim();
            String name = panel.getTxtUserDisplayname().getText().trim();
            String email = panel.getTxtEmail().getText().trim();
            String passwordRaw = panel.getTxtPassword().getText().trim();
            String password = null;
            if ( ! passwordRaw.equals("") ) {
                password = StringUtils.hashPassword(passwordRaw);
            }
            microServerProxy.updateUser(id, username, password, name, email);
        }
        getUsers();
    }

    private void addNewUser() {
        newUser = true;
        SwingUtilities.invokeLater(() -> {
            panel.getLblUserID().setText("");
            panel.getTxtUserDisplayname().setText("New User Name");
            panel.getTxtEmail().setText("the@email.com");
            panel.getTxtUsername().setText("username");
            panel.getBtnSaveUser().setEnabled(true);
            panel.getBtnDeleteUser().setEnabled(false);
        });
    }

    private void deleteUser() {
        Long id = Long.parseLong(panel.getLblUserID().getText());
        if ( id != null && id != 0 ) {
            microServerProxy.deleteUser(id);
        }
        empty(false);
        getUsers();
    }

    private void userSelected(Long userId) {
        try {
            UserDto userDto = microServerProxy.getUser(userId);
            if (userDto != null) {
                SwingUtilities.invokeLater(() -> {
                    panel.getLblUserID().setText("" + userDto.getId());
                    panel.getTxtUserDisplayname().setText(userDto.getName());
                    panel.getTxtEmail().setText(userDto.getEmail());
                    panel.getTxtUsername().setText(userDto.getUsername());
                    panel.getBtnSaveUser().setEnabled(true);
                    panel.getBtnDeleteUser().setEnabled(true);
                });
            }
        } catch (MicroServerException e) {
            JOptionPane.showMessageDialog(panel, e.getMessage());
        }
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
