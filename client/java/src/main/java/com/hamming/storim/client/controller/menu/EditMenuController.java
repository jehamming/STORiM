package com.hamming.storim.client.controller.menu;

import com.hamming.storim.client.STORIMWindow;
import com.hamming.storim.client.STORIMWindowController;
import com.hamming.storim.client.controller.*;
import com.hamming.storim.client.panels.*;
import com.hamming.storim.client.view.RoomTileMapEditorView;
import com.hamming.storim.common.MicroServerProxy;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginResultDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginWithTokenResultDTO;
import com.hamming.storim.common.dto.protocol.serverpush.SetRoomDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditMenuController implements ConnectionListener {

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
    private MicroServerProxy microServerProxy;

    public EditMenuController(STORIMWindow storimWindow, STORIMWindowController windowController, MicroServerProxy microServerProxy) {
        this.window = storimWindow;
        this.windowController = windowController;
        this.microServerProxy = microServerProxy;
        microServerProxy.getConnectionController().addConnectionListener(this);
        registerReceivers();
        setup();
    }

    private void registerReceivers() {
        microServerProxy.getConnectionController().registerReceiver(LoginResultDTO.class, (ProtocolReceiver<LoginResultDTO>) dto -> loginSuccess(dto.isSuccess()));
        microServerProxy.getConnectionController().registerReceiver(LoginWithTokenResultDTO.class, (ProtocolReceiver<LoginWithTokenResultDTO>) dto -> loginSuccess(dto.isSuccess()));
        microServerProxy.getConnectionController().registerReceiver(SetRoomDTO.class, (ProtocolReceiver<SetRoomDTO>) dto -> setRoom(dto.getRoom()));
    }

    private void setRoom(RoomDto room) {
        window.getMenuRoomTiles().setEnabled(room.isEditable());
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
        tileSetEditorPanelController = new TileSetEditorPanelController(windowController, tileSetEditorPanel, microServerProxy);
        //Prepare Frame
        tileSetEditorFrame = new JFrame("TileSets Editor");
        tileSetEditorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tileSetEditorFrame.getContentPane().add(tileSetEditorPanel);
        tileSetEditorFrame.pack();
        window.getMenuEditTileSets().addActionListener(e -> tileSetEditorFrame.setVisible(true));
        tileSetEditorFrame.setLocationRelativeTo(window);
    }

    private void setupRoomTileEditor() {
        //RoomTileMapEditor
        // Controller
        roomTileMapEditorPanelController = new RoomTileMapEditorPanelController(microServerProxy);
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
        roomEditorFrame.setLocationRelativeTo(window);
    }

    private void setupExitEditor() {
        exitPanel = new ExitPanel();
        exitPanelController = new ExitPanelController(windowController, exitPanel, microServerProxy);
        //Prepare Frame
        exitsFrame = new JFrame("Exit Editor");
        exitsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        exitsFrame.getContentPane().add(exitPanel);
        exitsFrame.pack();
        exitsFrame.setLocationRelativeTo(window);
        window.getMenuExits().addActionListener(e -> exitsFrame.setVisible(true));
    }

    private void setupThingEditor() {
        thingPanel = new ThingPanel();
        thingPanelController = new ThingPanelController(windowController, thingPanel, microServerProxy);
        //Prepare Frame
        thingFrame = new JFrame("Thing Editor");
        thingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        thingFrame.getContentPane().add(thingPanel);
        thingFrame.pack();
        thingFrame.setLocationRelativeTo(window);
        window.getMenuItems().addActionListener(e -> thingFrame.setVisible(true));
    }

    private void setupRoomEditor() {
        roomEditorPanel = new RoomEditorPanel();
        roomEditorPanelController = new RoomEditorPanelController(windowController, roomEditorPanel, microServerProxy);
        //Prepare Frame
        roomEditorFrame = new JFrame("Rooms Editor");
        roomEditorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        roomEditorFrame.getContentPane().add(roomEditorPanel);
        roomEditorFrame.pack();
        roomEditorFrame.setLocationRelativeTo(window);
        window.getMenuRooms().addActionListener(e -> roomEditorFrame.setVisible(true));
    }

    private void setupAvatarEditor() {
        avatarPanel = new AvatarPanel();
        avatarPanelController = new AvatarPanelController(windowController, avatarPanel, microServerProxy);
        //Prepare Frame
        avatarsFrame = new JFrame("Avatar Editor");
        avatarsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        avatarsFrame.getContentPane().add(avatarPanel);
        avatarsFrame.pack();
        avatarsFrame.setLocationRelativeTo(window);
        window.getMenuAvatars().addActionListener(e -> avatarsFrame.setVisible(true));
    }

    private void setupUserInfo() {
        userInfoPanel = new UserInfoPanel();
        userInfoPanelController = new UserInfoPanelController(windowController, userInfoPanel, microServerProxy);
        //Prepare Frame
        usersInfoFrame = new JFrame("User Info");
        usersInfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        usersInfoFrame.getContentPane().add(userInfoPanel);
        usersInfoFrame.pack();
        usersInfoFrame.setLocationRelativeTo(window);
        //TODO Add UserINfoMenu
        //window.getMenu().addActionListener(e -> usersInfoFrame.setVisible(true));
    }

    private void setUpVerbEditor() {
        verbEditorPanel = new VerbEditorPanel();
        verbEditorPanelController = new VerbEditorPanelController(windowController, verbEditorPanel, microServerProxy);
        //Prepare Frame
        verbsFrame = new JFrame("Verb Editor");
        verbsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        verbsFrame.getContentPane().add(verbEditorPanel);
        verbsFrame.pack();
        verbsFrame.setLocationRelativeTo(window);
        window.getMenuVerbs().addActionListener(e -> verbsFrame.setVisible(true));
    }

    public void disconnect() {
        SwingUtilities.invokeLater(() -> {
            window.getMenuConnect().setEnabled(true);
            window.getMenuDisconnect().setEnabled(false);
        });
        windowController.setCurrentServerId(null);
    }

    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {
        SwingUtilities.invokeLater(() -> {
            enableMenus(false);
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
