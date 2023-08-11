package com.hamming.storim.client;

import com.hamming.storim.client.controller.*;
import com.hamming.storim.client.controller.menu.FileMenuController;
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

public class STORIMWindowController implements ConnectionListener {

    private ConnectionController connectionController;
    private FileMenuController fileMenuController;

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
    private STORIMWindow window;


    public STORIMWindowController(STORIMWindow window, String username, String password ) {
        this.username = username;
        this.password = password;
        this.window = window;
        init();
    }

    private void init() {
        connectionController = new ConnectionController("STORIM_Java_client");
        connectionController.addConnectionListener(this);
        window.setTitle(BASIC_TITLE);
        gameView = new GameViewPanel(this);
        gameView.setPreferredSize(new Dimension(500,400));
        GameViewController viewController = new GameViewController(this, gameView, connectionController);
        gameView.setViewController(viewController);
        initComponents();
        initControllers();
        addActionlisteners();
        registerReceivers();
        window.setVisible(true);
        gameView.start();
    }

    private void initControllers() {
        fileMenuController = new FileMenuController(window, this, connectionController);
    }

    private void addActionlisteners() {

    }



    private void initComponents() {

        chatPanel = new ChatPanel();
        chatPanelController = new ChatPanelController(this , chatPanel, connectionController);

        verbEditorPanel = new VerbEditorPanel();
        verbEditorPanelController = new VerbEditorPanelController(this , verbEditorPanel, connectionController);


        userInfoPanel = new UserInfoPanel();
        userInfoPanelController = new UserInfoPanelController(this, userInfoPanel, connectionController);

        avatarPanel = new AvatarPanel();
        avatarPanelController = new AvatarPanelController(this, avatarPanel, connectionController);


        roomEditorPanel = new RoomEditorPanel();
        roomEditorPanelController = new RoomEditorPanelController(this, roomEditorPanel, connectionController);


        thingPanel = new ThingPanel();
        thingPanelController = new ThingPanelController(this, thingPanel, connectionController);

        exitPanel = new ExitPanel();
        exitPanelController = new ExitPanelController(this, exitPanel, connectionController);

        adminPanel = new AdminPanel();
        adminPanelController = new AdminPanelController(this, adminPanel, connectionController);

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

        window.setPnlGameView(gameView);
        window.setChatPanel(chatPanel);

        window.initComponents();

    }

    private void registerReceivers() {
        connectionController.registerReceiver(SetCurrentUserDTO.class, (ProtocolReceiver<SetCurrentUserDTO>) dto -> setCurrentUser(dto.getUser()));
    }

    public void setCurrentUser(UserDto currentUser) {
        this.currentUser = currentUser;
        if ( currentUser != null ) {
            window.setTitle(currentUser.getName());
        }
    }

    @Override
    public void connected() {
    }

    @Override
    public void disconnected() {
        window.setTitle(BASIC_TITLE);
    }

    public void setCurrentServerId(String currentServerId) {
        this.currentServerId = currentServerId;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public STORIMWindow getWindow() {
        return window;
    }

    public UserDto getCurrentUser() {
        return currentUser;
    }

    public String getCurrentServerId() {
        return currentServerId;
    }

    public void useExitToOtherServer(String serverURI) {
        fileMenuController.getLoginPanelController().connectToServer(currentUser.getId(), userToken, serverURI  );
    }

    public void setRoomname(String text) {
        SwingUtilities.invokeLater(() -> {
            window.getLblRoomName().setText(text);
        });
    }

    public FileMenuController getFileMenuController() {
        return fileMenuController;
    }
}
