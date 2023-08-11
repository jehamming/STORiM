package com.hamming.storim.client;

import com.hamming.storim.client.controller.*;
import com.hamming.storim.client.panels.*;
import com.hamming.storim.client.view.GameViewPanel;
import com.hamming.storim.client.view.RoomTileMapEditorView;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.serverpush.SetCurrentUserDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;

import javax.swing.*;
import java.awt.*;

public class STORIMWindowOld extends JFrame implements ConnectionListener {


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
    private ExitPanel exitPanel;
    private ExitPanelController exitPanelController;
    private AdminPanel adminPanel;
    private AdminPanelController adminPanelController;
    private RoomTileEditorPanel roomTileEditorPanel;
    private RoomTileMapEditorPanelController roomTileMapEditorPanelController;
    private RoomTileMapEditorView tileMapEditorView;


    private GameViewPanel gameView;
    private static String BASIC_TITLE = "STORIM";

    private UserDto currentUser;
    private String currentServerId;
    private String userToken;
    private String username;
    private String password;

    public STORIMWindowOld(ConnectionController connectionController, String username, String password) {
        this.username = username;
        this.password = password;
        this.connectionController = connectionController;
        connectionController.addConnectionListener(this);
        setTitle(BASIC_TITLE);
        gameView.setPreferredSize(new Dimension(1000,500));
        registerReceivers();
        setVisible(true);
    }

    private void initRoomTileEditor() {
        //RoomTileMapEditor
        // Controller
        roomTileMapEditorPanelController = new RoomTileMapEditorPanelController(connectionController );
        // View
        tileMapEditorView = new RoomTileMapEditorView(roomTileMapEditorPanelController);
        roomTileMapEditorPanelController.setRoomTileMapEditorView(tileMapEditorView);
        tileMapEditorView.setPreferredSize(new Dimension(500, 500));
        // Panel
        roomTileEditorPanel = new RoomTileEditorPanel(tileMapEditorView);
        roomTileMapEditorPanelController.setPanel(roomTileEditorPanel);
        roomTileMapEditorPanelController.setup();
    }

    public void showRoomEditorWindow() {
        JFrame frame = new JFrame();
        frame.getContentPane().add(roomTileEditorPanel);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setTitle("RoomTileEditor");
        frame.pack();
        frame.setVisible(true);

        tileMapEditorView.start();
    }

    private void registerReceivers() {
        connectionController.registerReceiver(SetCurrentUserDTO.class, (ProtocolReceiver<SetCurrentUserDTO>) dto -> setCurrentUser(dto.getUser()));
    }


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



    public UserDto getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserDto currentUser) {
        this.currentUser = currentUser;
        if ( currentUser != null ) {
            setTitle(currentUser.getName());
        }
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getCurrentServerId() {
        return currentServerId;
    }

    public void setCurrentServerId(String currentServerId) {
        this.currentServerId = currentServerId;
    }

    public void useExitToOtherServer(String serverURI) {
       loginPanelController.connectToServer(currentUser.getId(), userToken, serverURI  );
    }
}
