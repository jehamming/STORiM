package com.hamming.storim.client.controller.admin;

import com.hamming.storim.client.controller.AdminPanelController;
import com.hamming.storim.client.listitem.ShortUserListItem;
import com.hamming.storim.client.panels.AdminPanel;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.*;
import com.hamming.storim.common.dto.protocol.request.DeleteUserDto;
import com.hamming.storim.common.util.StringUtils;

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

        panel.getBtnNewUser().setEnabled(true);
        panel.getBtnSaveUser().setEnabled(false);
        panel.getBtnDeleteUser().setEnabled(false);
    }

    private void getUsers() {
        SwingUtilities.invokeLater(() -> {
            usersModel.removeAllElements();
            GetUsersResultDTO response = connectionController.sendReceive(new GetUsersRequestDTO(), GetUsersResultDTO.class);
            if (response != null) {
                for (Long userId : response.getUsers().keySet()) {
                    String userName = response.getUsers().get(userId);
                    ShortUserListItem item = new ShortUserListItem(userId, userName);
                    usersModel.addElement(item);
                }
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

            AddUserDto addUserDto = new AddUserDto(username, password, name, email);
            AddUserResultDTO addUserResultDTO = connectionController.sendReceive(addUserDto, AddUserResultDTO.class);
            if ( !addUserResultDTO.isSuccess() ) {
                JOptionPane.showMessageDialog(panel, addUserResultDTO.getErrorMessage());
            } else {
                newUser = false;
                empty(false);
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
            UpdateUserDto updateUserDto = new UpdateUserDto(id, username, password, name, email, null);
            connectionController.send(updateUserDto);
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
            DeleteUserDto dto = new DeleteUserDto(id);
            connectionController.send(dto);
        }
        empty(false);
        getUsers();
    }

    private void userSelected(Long userId) {
        GetUserDTO request = new GetUserDTO(userId);
        GetUserResultDTO resultDTO = connectionController.sendReceive(request, GetUserResultDTO.class);
        if (resultDTO.getUser() != null) {
            SwingUtilities.invokeLater(() -> {
                UserDto user = resultDTO.getUser();
                panel.getLblUserID().setText("" + user.getId());
                panel.getTxtUserDisplayname().setText(user.getName());
                panel.getTxtEmail().setText(user.getEmail());
                panel.getTxtUsername().setText(user.getUsername());
                panel.getBtnSaveUser().setEnabled(true);
                panel.getBtnDeleteUser().setEnabled(true);
            });
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
