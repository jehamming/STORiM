package com.hamming.storim.client;

import com.hamming.storim.client.controller.LoginPanelController;
import com.hamming.storim.client.panels.*;
import com.hamming.storim.client.view.GameViewPanel;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.client.controller.GameViewController;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.interfaces.ConnectionListener;

import javax.swing.*;

public class STORIMWindow extends JFrame implements ConnectionListener {


    private LoginPanel loginPanel;
    private LoginPanelController loginPanelController;

    private ConnectionController connectionController;
    private UserInfoPanel userInfoPanel;
    private ChatPanel chatPanel;
    private VerbEditorPanel verbEditorPanel;
    private RoomEditorPanel roomEditorPanel;
    private AvatarPanel avatarPanel;
    private ThingPanel thingPanel;
    private ExitEditorPanel exitEditorPanel;
    private GameViewPanel gameView;
    private JTabbedPane tabbedPane;
    private  JLabel lblRoomname;
    private static String BASIC_TITLE = "STORIM Java Client";

    private UserDto currentUser;
    private String userToken;

    public STORIMWindow(ConnectionController connectionController) {
        this.connectionController = connectionController;
        connectionController.addConnectionListener(this);
        setTitle(BASIC_TITLE);
        gameView = new GameViewPanel(this);
        GameViewController viewController = new GameViewController(this, gameView, connectionController);
        gameView.setViewController(viewController);
        initComponents();
        addTabs();
        emptyPanels();
        pack();
        setVisible(true);
    }

    public void addTabs() {
        loginPanel = new LoginPanel();
        loginPanelController = new LoginPanelController(this, loginPanel, connectionController);
        tabbedPane.addTab("Connect/Disconnect", loginPanel);

//        chatPanel = new ChatPanel(controllers);
//        //mainPanel.add(chatPanel);
//        tabbedPane.addTab("Chat", chatPanel);
//
//        userInfoPanel = new UserInfoPanel(controllers);
//        //mainPanel.add(userInfoPanel);
//        tabbedPane.addTab("Users", userInfoPanel);
//
//        verbEditorPanel = new VerbEditorPanel(controllers);
//        //mainPanel.add(verbEditorPanel);
//        tabbedPane.addTab("Verbs", verbEditorPanel);
//
//        roomEditorPanel = new RoomEditorPanel(controllers);
//        tabbedPane.addTab("Rooms", roomEditorPanel);
//
//        avatarPanel = new AvatarPanel(controllers);
//        tabbedPane.add("Avatars", avatarPanel);
//
//        thingPanel = new ThingPanel(controllers);
//        tabbedPane.add("Things", thingPanel);
//
//        exitEditorPanel = new ExitEditorPanel(controllers);
//        tabbedPane.add("Exits", exitEditorPanel);

    }


    private void initComponents() {

        lblRoomname = new javax.swing.JLabel();
        tabbedPane = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        gameView.setPreferredSize(new java.awt.Dimension(430, 430));

        javax.swing.GroupLayout gameViewLayout = new javax.swing.GroupLayout(gameView);
        gameView.setLayout(gameViewLayout);
        gameViewLayout.setHorizontalGroup(
                gameViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );
        gameViewLayout.setVerticalGroup(
                gameViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 556, Short.MAX_VALUE)
        );

        lblRoomname.setText("Room name");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                                        .addComponent(lblRoomname, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
                        .addComponent(gameView, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(gameView, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblRoomname, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pack();
    }


    public void emptyPanels() {
        //FIXME
//        userInfoPanel.empty();
//        chatPanel.empty();
//        verbEditorPanel.empty();
//        roomEditorPanel.empty(true);
//        avatarPanel.empty(true);
    }


    @Override
    public void connected() {
    }

    @Override
    public void disconnected() {
        emptyPanels();
        setTitle(BASIC_TITLE);
    }


    public GameViewPanel getGameView() {
        return gameView;
    }

    public void setRoomname(String roomName) {
        //TODO - CurrentUser!
        String text = "User: "+ currentUser.getName() + ", room :" + roomName;
        SwingUtilities.invokeLater(() -> {
            lblRoomname.setText(text);
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
