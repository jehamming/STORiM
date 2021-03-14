package nl.hamming.storimapp;

import com.hamming.storim.Controllers;
import com.hamming.storim.interfaces.ConnectionListener;
import com.hamming.storim.interfaces.UserListener;
import com.hamming.storim.model.dto.LocationDto;
import com.hamming.storim.model.dto.UserDto;
import nl.hamming.storimapp.panels.*;
import nl.hamming.storimapp.view.GameView;
import nl.hamming.storimapp.view.ViewController;

import javax.swing.*;

public class STORIMWindow extends JFrame implements ConnectionListener, UserListener {

    private UserInfoPanel userInfoPanel;
    private LoginPanel loginPanel;
    private ChatPanel chatPanel;
    private VerbEditorPanel verbEditorPanel;
    private RoomEditorPanel roomEditorPanel;
    private Controllers controllers;
    private GameView gameView;
    private JTabbedPane tabbedPane;
    private static String BASIC_TITLE = "STORIM Java Client";

    public STORIMWindow(Controllers controllers) {
        this.controllers = controllers;
        controllers.getConnectionController().addConnectionListener(this);
        controllers.getUserController().addUserListener(this);
        setTitle(BASIC_TITLE);
        gameView = new GameView();
        gameView.setViewController(new ViewController(getGameView(), controllers));
        initComponents();
        addTabs();
        emptyPanels();
        pack();
        setVisible(true);
    }

    public void addTabs() {
        loginPanel = new LoginPanel(controllers);
        tabbedPane.addTab("Connect/Disconnect", loginPanel);

        chatPanel = new ChatPanel(controllers);
        //mainPanel.add(chatPanel);
        tabbedPane.addTab("Chat", chatPanel);

        userInfoPanel = new UserInfoPanel(controllers);
        //mainPanel.add(userInfoPanel);
        tabbedPane.addTab("Users", userInfoPanel);

        verbEditorPanel = new VerbEditorPanel(controllers);
        //mainPanel.add(verbEditorPanel);
        tabbedPane.addTab("Verbs", verbEditorPanel);


        roomEditorPanel = new RoomEditorPanel(controllers);
        tabbedPane.addTab("Rooms", roomEditorPanel);
    }


    private void initComponents() {

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
                        .addGap(0, 550, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(gameView, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                                        .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(gameView, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pack();
    }


    public void emptyPanels() {
        userInfoPanel.empty();
        chatPanel.empty();
        verbEditorPanel.empty();
        roomEditorPanel.empty(true);
    }


    public GameView getGameView() {
        return gameView;
    }

    @Override
    public void connected() {
    }

    @Override
    public void disconnected() {
        emptyPanels();
        setTitle(BASIC_TITLE);
    }

    @Override
    public void userConnected(UserDto user) {

    }

    @Override
    public void userDisconnected(UserDto user) {

    }

    @Override
    public void userOnline(UserDto user) {

    }

    @Override
    public void loginResult(boolean success, String message) {
        if (success) {
            setTitle(BASIC_TITLE + " - " +  controllers.getUserController().getCurrentUser().getName());
            tabbedPane.setSelectedIndex(1);
        }
    }

    @Override
    public void userTeleported(Long userId, LocationDto location) {

    }

}