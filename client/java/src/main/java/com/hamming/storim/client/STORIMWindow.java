package com.hamming.storim.client;

import com.hamming.storim.client.controller.*;
import com.hamming.storim.client.panels.*;
import com.hamming.storim.client.view.GameViewPanel;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.interfaces.ConnectionListener;

import javax.swing.*;
import java.awt.*;

public class STORIMWindow extends JFrame implements ConnectionListener {


    private LoginPanel loginPanel;
    private LoginPanelController loginPanelController;
    private ChatPanel chatPanel;
    private ChatPanelController chatPanelController;
    private VerbEditorPanel verbEditorPanel;
    private VerbEditorPanelController verbEditorPanelController;
    private UserInfoPanel userInfoPanel;
    private UserInfoPanelController userInfoPanelController;
    private AvatarPanel avatarPanel;
    private AvatarPanelController avatarPanelController;
    private RoomEditorPanel roomEditorPanel;
    private RoomEditorPanelController roomEditorPanelController;
    private ThingPanelController thingPanelController;
    private ConnectionController connectionController;
    private ThingPanel thingPanel;


    private ExitEditorPanel exitEditorPanel;

    private GameViewPanel gameView;
    private static String BASIC_TITLE = "STORIM Java Client";

    private UserDto currentUser;
    private String userToken;

    public STORIMWindow(ConnectionController connectionController) {
        this.connectionController = connectionController;
        connectionController.addConnectionListener(this);
        setTitle(BASIC_TITLE);
        gameView = new GameViewPanel(this);
        gameView.setPreferredSize(new Dimension(1000,500));
        GameViewController viewController = new GameViewController(this, gameView, connectionController);
        gameView.setViewController(viewController);
        initComponents();
        addTabs();
        setVisible(true);
    }

    public void addTabs() {
        loginPanel = new LoginPanel();
        loginPanelController = new LoginPanelController(this, loginPanel, connectionController);
        tabbedPane.addTab("Connect/Disconnect", loginPanel);

        chatPanel = new ChatPanel();
        chatPanelController = new ChatPanelController(this , chatPanel, connectionController);
        tabbedPane.addTab("Chat", chatPanel);

        verbEditorPanel = new VerbEditorPanel();
        verbEditorPanelController = new VerbEditorPanelController(this , verbEditorPanel, connectionController);
        tabbedPane.addTab("Verbs", verbEditorPanel);


        userInfoPanel = new UserInfoPanel();
        userInfoPanelController = new UserInfoPanelController(this, userInfoPanel, connectionController);
        tabbedPane.addTab("Users", userInfoPanel);

        avatarPanel = new AvatarPanel();
        avatarPanelController = new AvatarPanelController(this, avatarPanel, connectionController);
        tabbedPane.add("Avatars", avatarPanel);


        roomEditorPanel = new RoomEditorPanel();
        roomEditorPanelController = new RoomEditorPanelController(this, roomEditorPanel, connectionController);
        tabbedPane.addTab("Rooms", roomEditorPanel);


        thingPanel = new ThingPanel();
        thingPanelController = new ThingPanelController(this, thingPanel, connectionController);
        tabbedPane.addTab("Things", thingPanel);
//
//        thingPanel = new ThingPanel(controllers);
//        tabbedPane.add("Things", thingPanel);
//
//        exitEditorPanel = new ExitEditorPanel(controllers);
//        tabbedPane.add("Exits", exitEditorPanel);

    }


    private void initComponents() {
        lblRoomName = new javax.swing.JLabel();
        tabbedPane = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1200, 570));
        setPreferredSize(new java.awt.Dimension(1200, 570));

        gameView.setPreferredSize(new java.awt.Dimension(500, 500));

        javax.swing.GroupLayout gameViewLayout = new javax.swing.GroupLayout(gameView);
        gameView.setLayout(gameViewLayout);
        gameViewLayout.setHorizontalGroup(
                gameViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 500, Short.MAX_VALUE)
        );
        gameViewLayout.setVerticalGroup(
                gameViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 500, Short.MAX_VALUE)
        );

        tabbedPane.setPreferredSize(new java.awt.Dimension(700, 500));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(gameView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(lblRoomName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(lblRoomName, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(gameView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleDescription("");

        pack();

    }

    private javax.swing.JLabel lblRoomName;
    private javax.swing.JTabbedPane tabbedPane;


    @Override
    public void connected() {
    }

    @Override
    public void disconnected() {
        setTitle(BASIC_TITLE);
    }


    public GameViewPanel getGameView() {
        return gameView;
    }

    public void setRoomname(String roomName) {
        //TODO - CurrentUser!
        String text = "User: "+ currentUser.getName() + ", room :" + roomName;
        SwingUtilities.invokeLater(() -> {
            lblRoomName.setText(text);
        });
    }

    public UserDto getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserDto currentUser) {
        this.currentUser = currentUser;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
