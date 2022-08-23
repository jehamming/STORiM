package com.hamming.storim.client.controller;

import com.hamming.storim.client.STORIMWindow;
import com.hamming.storim.client.listitem.UserListItem;
import com.hamming.storim.client.listitem.VerbListItem;
import com.hamming.storim.client.panels.UserInfoPanel;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.serverpush.UserDisconnectedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserOnlineDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

public class UserInfoPanelController implements ConnectionListener {

    private ConnectionController connectionController;
    private UserInfoPanel panel;
    private STORIMWindow storimWindow;
    private DefaultListModel<UserListItem> onlineUsersListmodel;
    private DefaultListModel<VerbListItem> verbsListmodel;

    public UserInfoPanelController(STORIMWindow storimWindow, UserInfoPanel panel, ConnectionController connectionController) {
        this.panel = panel;
        this.storimWindow = storimWindow;
        this.connectionController = connectionController;
        connectionController.addConnectionListener(this);
        registerReceivers();
        setup();
    }

    private void registerReceivers() {
        connectionController.registerReceiver(UserDisconnectedDTO.class, (ProtocolReceiver<UserDisconnectedDTO>) dto -> userDisconnected(dto));
        connectionController.registerReceiver(UserOnlineDTO.class, (ProtocolReceiver<UserOnlineDTO>) dto -> userOnline(dto));
    }

    private void userOnline(UserOnlineDTO dto) {
        //TODO
    }


    private void setup() {
        onlineUsersListmodel = new DefaultListModel();
        panel.getListOnlineUsers().setModel(onlineUsersListmodel);
        panel.getListOnlineUsers().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if ( !e.getValueIsAdjusting() ) { //Else this is called twice!
                    UserListItem item = (UserListItem) panel.getListOnlineUsers().getSelectedValue();
                    if (item != null && item.getUser()!= null) {
                        userSelected(item.getUser());
                    }
                }
            }
        });
        verbsListmodel = new DefaultListModel();
        panel.getListVerbs().setModel(verbsListmodel);
        panel.getListVerbs().addListSelectionListener(e -> {
            if ( !e.getValueIsAdjusting() ) { //Else this is called twice!
                VerbListItem item = panel.getListVerbs().getSelectedValue();
                if (item != null) {
                    verbSelected(item.getId());
                }
            }
        });
        panel.getBtnTeleport().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teleport();
            }
        });
    }

    private void userDisconnected(UserDisconnectedDTO dto) {
        //TODO
    }



    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {
        panel.empty();
    }

    private void teleport() {
        //FIXME teleport
//        UserListItem item = panel.getListOnlineUsers().getSelectedValue();
//        LocationDto location = controllers.getUserController().getUserLocation(item.getUser().getId());
//        controllers.getRoomController().teleportRequest(controllers.getUserController().getCurrentUser().getId(), location.getRoomId());
    }

    private void verbSelected(Long verbId) {
        // TODO Implement

    }


    private void userSelected(UserDto user) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                panel.getLblUserID().setText(user.getId().toString());
                panel.getLblUserName().setText(user.getName());
                //FIXME : Get the location of the user
                panel.getLblLocation().setText("TBS");
            }
        });
    }

    private void addUser(UserDto user) {
        if (!contains(user)) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    UserListItem item = new UserListItem(user);
                    onlineUsersListmodel.addElement(item);
                }
            });
        }
    }

    private void removeUser(UserDto user) {
        if (contains(user)) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    UserListItem item = findListItemFor(user);
                    onlineUsersListmodel.removeElement(item);
                }
            });
        }
    }

    public boolean contains(UserDto dto) {
        return findUser(dto) != null;
    }

    public UserDto findUser(UserDto dto) {
        UserDto found=null;
        Enumeration<UserListItem> enumerator = onlineUsersListmodel.elements();
        while (enumerator.hasMoreElements()) {
            UserListItem item = enumerator.nextElement();
            if (item.getUser().getId().equals(dto.getId())) {
                found = item.getUser();
            }
        }
        return found;
    }

    public UserListItem findListItemFor(UserDto dto) {
        UserListItem found=null;
        Enumeration<UserListItem> enumerator = onlineUsersListmodel.elements();
        while (enumerator.hasMoreElements()) {
            UserListItem item = enumerator.nextElement();
            if (item.getUser().getId().equals(dto.getId())) {
                found = item;
            }
        }
        return found;
    }

    public void userTeleported(Long userId) {
        UserListItem item = panel.getListOnlineUsers().getSelectedValue();
        if ( item != null ) {
            if ( item.getUser().getId().equals(userId)) {
                SwingUtilities.invokeLater(() -> {
                    //FIXME get location room name
                    panel.getLblLocation().setText("TBS");
                });
            }
        }
    }


    public void userSelectedInView(UserDto user) {
        SwingUtilities.invokeLater(() -> {
            UserListItem item = getItem(user);
            if ( item != null ) {
                panel.getListOnlineUsers().setSelectedValue(item, true);
            }
        });
    }


    private UserListItem getItem(UserDto user) {
        UserListItem found = null;
        for (int i = 0; i < onlineUsersListmodel.getSize(); i++) {
            UserListItem item = onlineUsersListmodel.get(i);
            if (item.getUser().equals(user)) {
                found = item;
                break;
            }
        }
        return found;
    }

}
