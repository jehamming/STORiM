package nl.hamming.storimapp;

import com.hamming.storim.Controllers;
import com.hamming.storim.controllers.MoveController;
import com.hamming.storim.controllers.UserController;
import com.hamming.storim.interfaces.ConnectionListener;
import com.hamming.storim.interfaces.UserListener;
import com.hamming.storim.model.dto.LocationDto;
import com.hamming.storim.model.dto.UserDto;
import nl.hamming.storimapp.panels.*;
import nl.hamming.storimapp.view.GameView;
import nl.hamming.storimapp.view.ViewController;

import javax.swing.*;
import java.awt.*;

public class STORIMWindow extends JFrame implements ConnectionListener, UserListener {

    private UserInfoPanel userInfoPanel;
    private LoginPanel loginPanel;
    private ChatPanel chatPanel;
    private MovementPanel movementPanel;
    private VerbEditorPanel verbEditorPanel;
    private RoomEditorPanel roomEditorPanel;
    private Controllers controllers;
    private GameView gameView;
    private JTabbedPane tabbedPane;

    public STORIMWindow(Controllers controllers) {
        this.controllers = controllers;
        controllers.getConnectionController().addConnectionListener(this);
        controllers.getUserController().addUserListener(this);
        setTitle("STORIM Java Client");
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

        javax.swing.GroupLayout gameViewLayout = new javax.swing.GroupLayout(gameView);
        gameView.setLayout(gameViewLayout);
        gameViewLayout.setHorizontalGroup(
                gameViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );
        gameViewLayout.setVerticalGroup(
                gameViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 347, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(gameView, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 739, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(gameView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
                                .addContainerGap())
        );

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
            tabbedPane.setSelectedIndex(1);
        }
    }

    @Override
    public void currentUserLocation(LocationDto loc) {

    }

    @Override
    public void userLocationUpdate(Long userId, LocationDto loc) {

    }
}
