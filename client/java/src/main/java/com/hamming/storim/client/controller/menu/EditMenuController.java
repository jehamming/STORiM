package com.hamming.storim.client.controller.menu;

import com.hamming.storim.client.STORIMWindow;
import com.hamming.storim.client.STORIMWindowController;
import com.hamming.storim.client.controller.*;
import com.hamming.storim.client.panels.*;
import com.hamming.storim.client.view.RoomTileMapEditorView;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginResultDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditMenuController implements ConnectionListener {

    private ConnectionController connectionController;
    private STORIMWindow window;
    private STORIMWindowController windowController;
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
    private RoomTileEditorPanel roomTileEditorPanel;
    private RoomTileMapEditorPanelController roomTileMapEditorPanelController;
    private RoomTileMapEditorView tileMapEditorView;

    private TileSetEditorPanel tileSetEditorPanel;
    private TileSetEditorPanelController tileSetEditorPanelController;

    private JFrame verbsFrame;
    private JFrame avatarsFrame;
    private JFrame exitsFrame;
    private JFrame usersInfoFrame;
    private JFrame roomEditorFrame;
    private JFrame thingFrame;
    private JFrame roomTileEditorFrame;
    private JFrame tileSetEditorFrame;

    public EditMenuController(STORIMWindow storimWindow, STORIMWindowController windowController, ConnectionController connectionController) {
        this.window = storimWindow;
        this.windowController = windowController;
        this.connectionController = connectionController;
        connectionController.addConnectionListener(this);
        registerReceivers();
        setup();
    }

    private void registerReceivers() {
        connectionController.registerReceiver(LoginResultDTO.class, (ProtocolReceiver<LoginResultDTO>) dto -> loginSuccess(dto.isSuccess()));
    }


    private void setup() {
        window.getMenuConnect().setEnabled(true);
        window.getMenuDisconnect().setEnabled(false);

        setUpVerbEditor();
        setupUserInfo();
        setupAvatarEditor();
        setupRoomEditor();
        setupThingEditor();
        setupExitEditor();
        setupRoomTileEditor();
        setupTileSetEditor();

        window.getMenuDisconnect().addActionListener(e -> disconnect());

        enableMenus(false);

    }

    private void setupTileSetEditor() {
        tileSetEditorPanel = new TileSetEditorPanel();
        tileSetEditorPanelController = new TileSetEditorPanelController(windowController, tileSetEditorPanel, connectionController);
        //Prepare Frame
        tileSetEditorFrame = new JFrame("TileSets Editor");
        tileSetEditorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tileSetEditorFrame.getContentPane().add(tileSetEditorPanel);
        tileSetEditorFrame.pack();
        window.getMenuEditTileSets().addActionListener(e -> tileSetEditorFrame.setVisible(true));
    }

    private void setupRoomTileEditor() {
        //RoomTileMapEditor
        // Controller
        roomTileMapEditorPanelController = new RoomTileMapEditorPanelController(connectionController);
        // View
        tileMapEditorView = new RoomTileMapEditorView(roomTileMapEditorPanelController);
        roomTileMapEditorPanelController.setRoomTileMapEditorView(tileMapEditorView);
        tileMapEditorView.setPreferredSize(new Dimension(500, 500));
        // Panel
        roomTileEditorPanel = new RoomTileEditorPanel(tileMapEditorView);
        roomTileMapEditorPanelController.setPanel(roomTileEditorPanel);
        roomTileMapEditorPanelController.setup();
        //Prepare Frame
        roomTileEditorFrame = new JFrame("Room Tile Editor");
        roomTileEditorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        roomTileEditorFrame.getContentPane().add(roomTileEditorPanel);
        roomTileEditorFrame.pack();
        window.getMenuRoomTiles().addActionListener(e -> {
                roomTileMapEditorPanelController.reset();
                roomTileEditorFrame.setVisible(true);
                });
        tileMapEditorView.start();
    }

    private void setupExitEditor() {
        exitPanel = new ExitPanel();
        exitPanelController = new ExitPanelController(windowController, exitPanel, connectionController);
        //Prepare Frame
        exitsFrame = new JFrame("Exit Editor");
        exitsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        exitsFrame.getContentPane().add(exitPanel);
        exitsFrame.pack();
        window.getMenuExits().addActionListener(e -> exitsFrame.setVisible(true));
    }

    private void setupThingEditor() {
        thingPanel = new ThingPanel();
        thingPanelController = new ThingPanelController(windowController, thingPanel, connectionController);
        //Prepare Frame
        thingFrame = new JFrame("Thing Editor");
        thingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        thingFrame.getContentPane().add(thingPanel);
        thingFrame.pack();
        window.getMenuItems().addActionListener(e -> thingFrame.setVisible(true));
    }

    private void setupRoomEditor() {
        roomEditorPanel = new RoomEditorPanel();
        roomEditorPanelController = new RoomEditorPanelController(windowController, roomEditorPanel, connectionController);
        //Prepare Frame
        roomEditorFrame = new JFrame("Rooms Editor");
        roomEditorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        roomEditorFrame.getContentPane().add(roomEditorPanel);
        roomEditorFrame.pack();
        window.getMenuRooms().addActionListener(e -> roomEditorFrame.setVisible(true));
    }

    private void setupAvatarEditor() {
        avatarPanel = new AvatarPanel();
        avatarPanelController = new AvatarPanelController(windowController, avatarPanel, connectionController);
        //Prepare Frame
        avatarsFrame = new JFrame("Avatar Editor");
        avatarsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        avatarsFrame.getContentPane().add(avatarPanel);
        avatarsFrame.pack();
        window.getMenuAvatars().addActionListener(e -> avatarsFrame.setVisible(true));
    }

    private void setupUserInfo() {
        userInfoPanel = new UserInfoPanel();
        userInfoPanelController = new UserInfoPanelController(windowController, userInfoPanel, connectionController);
        //Prepare Frame
        usersInfoFrame = new JFrame("User Info");
        usersInfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        usersInfoFrame.getContentPane().add(userInfoPanel);
        usersInfoFrame.pack();
        //TODO Add UserINfoMenu
        //window.getMenu().addActionListener(e -> usersInfoFrame.setVisible(true));
    }

    private void setUpVerbEditor() {
        verbEditorPanel = new VerbEditorPanel();
        verbEditorPanelController = new VerbEditorPanelController(windowController, verbEditorPanel, connectionController);
        //Prepare Frame
        verbsFrame = new JFrame("Verb Editor");
        verbsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        verbsFrame.getContentPane().add(verbEditorPanel);
        verbsFrame.pack();
        window.getMenuVerbs().addActionListener(e -> verbsFrame.setVisible(true));
    }

    public void disconnect() {
        connectionController.disconnect();
        SwingUtilities.invokeLater(() -> {
            window.getMenuConnect().setEnabled(true);
            ;
            window.getMenuDisconnect().setEnabled(false);
            ;
        });
        windowController.setCurrentServerId(null);
    }

    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {
        SwingUtilities.invokeLater(() -> {
            loginSuccess(false);
        });
    }

    private void loginSuccess(boolean success) {
        enableMenus(success);
    }

    private void enableMenus(boolean enable) {
        window.getMenuVerbs().setEnabled(enable);
        window.getMenuAvatars().setEnabled(enable);
        window.getMenuExits().setEnabled(enable);
        window.getMenuRooms().setEnabled(enable);
        window.getMenuRoomTiles().setEnabled(enable);
        window.getMenuItems().setEnabled(enable);
        window.getMenuEditTileSets().setEnabled(enable);
    }
}
