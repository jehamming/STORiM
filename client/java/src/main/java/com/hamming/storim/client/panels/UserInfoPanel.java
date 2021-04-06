package com.hamming.storim.client.panels;

import com.hamming.storim.common.Controllers;
import com.hamming.storim.common.ProtocolHandler;
import com.hamming.storim.common.interfaces.UserListener;
import com.hamming.storim.common.interfaces.ViewListener;
import com.hamming.storim.common.dto.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

/**
 *
 * @author jehamming
 */
public class UserInfoPanel extends javax.swing.JPanel implements UserListener, ViewListener {

    private DefaultListModel<UserListItem> onlineUsersListmodel;
    private DefaultListModel verbsListmodel;
    private ProtocolHandler protocolHandler;
    private Controllers controllers;


    private class UserListItem {
        private UserDto user;
        public UserListItem(UserDto usr) {
            this.user = usr;
        }
        @Override
        public String toString() {
            return user.getName();
        }
        public UserDto getUser() {
            return user;
        }
    }
    private class VerbListItem {
        private VerbDto verb;
        public VerbListItem(VerbDto verb) {
            this.verb = verb;
        }
        @Override
        public String toString() {
            return verb.getName();
        }
        public VerbDto getVerb() {
            return verb;
        }
    }


    /**
     * Creates new form UserInfoPanel
     */
    public UserInfoPanel(Controllers controllers) {
        this.controllers = controllers;
        controllers.getUserController().addUserListener(this);
        controllers.getViewerController().addViewListener(this);
        protocolHandler = new ProtocolHandler();
        setBorder(new TitledBorder("Users"));
        initComponents();
        setup();
    }

    private void setup() {
        onlineUsersListmodel = new DefaultListModel();
        listOnlineUsers.setModel(onlineUsersListmodel);
        listOnlineUsers.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if ( !e.getValueIsAdjusting() ) { //Else this is called twice!
                    UserListItem item = (UserListItem) listOnlineUsers.getSelectedValue();
                    if (item != null && item.getUser()!= null) {
                        userSelected(item.getUser());
                    }
                }
            }
        });
        verbsListmodel = new DefaultListModel();
        listVerbs.setModel(verbsListmodel);
        listVerbs.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if ( !e.getValueIsAdjusting() ) { //Else this is called twice!
                    VerbListItem item = (VerbListItem) listVerbs.getSelectedValue();
                    if (item != null && item.getVerb()!= null) {
                        verbSelected(item.getVerb());
                    }
                }
            }
        });
        btnTeleport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teleport();
            }
        });

    }

    private void teleport() {
        UserListItem item = listOnlineUsers.getSelectedValue();
        LocationDto location = controllers.getUserController().getUserLocation(item.getUser().getId());
        RoomDto roomDto = controllers.getRoomController().findRoomByID(location.getRoomId());
        controllers.getRoomController().teleportRequest(controllers.getUserController().getCurrentUser(), roomDto);
    }

    private void verbSelected(VerbDto verb) {
        // TODO Implement

    }


    private void userSelected(UserDto user) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                lblUserID.setText(user.getId().toString());
                lblUserName.setText(user.getName());
                LocationDto location = controllers.getUserController().getUserLocation(user.getId());
                RoomDto roomDto = controllers.getRoomController().findRoomByID(location.getRoomId());
                lblLocation.setText(roomDto.getName());
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


    public void empty() {
        onlineUsersListmodel.removeAllElements();
        lblUserID.setText("");
        lblUserName.setText("");
        lblLocation.setText("");
    }



    @Override
    public void userConnected(UserDto user) {
        addUser(user);
    }

    @Override
    public void userUpdated(UserDto user) {

    }

    @Override
    public void userDisconnected(UserDto user) {
        removeUser(user);
    }

    @Override
    public void userOnline(UserDto user) {
        addUser(user);
    }

    @Override
    public void loginResult(boolean success, String message) {

    }

    @Override
    public void userTeleported(Long userId, LocationDto location) {
        UserListItem item = listOnlineUsers.getSelectedValue();
        if ( item != null ) {
            if ( item.getUser().getId().equals(userId)) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        lblUserID.setText(item.getUser().getId().toString());
                        lblUserName.setText(item.getUser().getName());
                        RoomDto roomDto = controllers.getRoomController().findRoomByID(location.getRoomId());
                        lblLocation.setText(roomDto.getName());
                    }
                });
            }
        }
    }

    @Override
    public void avatarAdded(AvatarDto avatar) {

    }

    @Override
    public void avatarDeleted(AvatarDto avatar) {

    }

    @Override
    public void avatarUpdated(AvatarDto avatar) {

    }


    @Override
    public void thingSelectedInView(ThingDto thing) {

    }


    @Override
    public void userSelectedInView(UserDto user) {
        SwingUtilities.invokeLater(() -> {
            UserListItem item = getItem(user);
            if ( item != null ) {
                listOnlineUsers.setSelectedValue(item, true);
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


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblUserID = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listVerbs = new javax.swing.JList<>();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listThings = new javax.swing.JList<>();
        btnCopyVerb = new javax.swing.JButton();
        btnCopyThing = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        lblLocation = new javax.swing.JLabel();
        btnTeleport = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        listOnlineUsers = new javax.swing.JList<>();

        jLabel1.setText("User ID");

        jLabel2.setText("User Name");

        lblUserID.setText("User ID");

        lblUserName.setText("User Name");

        jLabel3.setText("Verbs");

        jScrollPane1.setViewportView(listVerbs);

        jLabel4.setText("Things");

        listThings.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(listThings);

        btnCopyVerb.setText("Copy");

        btnCopyThing.setText("Copy");

        jLabel5.setText("User Location");

        lblLocation.setText("User Location");

        btnTeleport.setText("Teleport");

        jLabel6.setText("Online Users:");
        jScrollPane3.setViewportView(listOnlineUsers);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel6)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jScrollPane2))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addGap(136, 136, 136)
                                                .addComponent(jLabel4))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnCopyVerb, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnCopyThing, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel2)
                                                        .addComponent(jLabel1)
                                                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))
                                                .addGap(12, 12, 12)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(lblLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(btnTeleport))
                                                        .addComponent(lblUserID, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lblUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(lblUserID)
                                        .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel2)
                                                        .addComponent(lblUserName))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lblLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnTeleport)
                                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel3)
                                                        .addComponent(jLabel4))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(jScrollPane3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnCopyVerb)
                                        .addComponent(btnCopyThing))
                                .addContainerGap())
        );
    }

    // Variables declaration - do not modify
    private javax.swing.JButton btnCopyThing;
    private javax.swing.JButton btnCopyVerb;
    private javax.swing.JButton btnTeleport;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblLocation;
    private javax.swing.JLabel lblUserID;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JList<String> listThings;
    private javax.swing.JList<VerbListItem> listVerbs;
    private javax.swing.JList<UserListItem> listOnlineUsers;
    // End of variables declaration
}
