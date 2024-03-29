package com.hamming.storim.client;

import com.hamming.storim.client.controller.*;
import com.hamming.storim.client.controller.menu.AdminMenuController;
import com.hamming.storim.client.controller.menu.EditMenuController;
import com.hamming.storim.client.controller.menu.FileMenuController;
import com.hamming.storim.client.panels.*;
import com.hamming.storim.client.view.GameViewPanel;
import com.hamming.storim.client.view.RoomTileMapEditorView;
import com.hamming.storim.common.MicroServerProxy;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.serverpush.SetCurrentUserDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class STORIMWindowController implements ConnectionListener {

    private ConnectionController connectionController;
    private MicroServerProxy microServerProxy;
    private FileMenuController fileMenuController;
    private EditMenuController editMenuController;
    private AdminMenuController adminMenuController;

    private ChatPanel chatPanel;
    private ChatPanelController chatPanelController;

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
        microServerProxy = new MicroServerProxy(connectionController);
        connectionController.addConnectionListener(this);
        window.setTitle(BASIC_TITLE);
        gameView = new GameViewPanel(this);
        gameView.setPreferredSize(new Dimension(500,400));
        GameViewController viewController = new GameViewController(this, gameView, microServerProxy);
        gameView.setViewController(viewController);
        initComponents();
        initControllers();
        registerReceivers();
        window.setVisible(true);
        gameView.start();
    }

    private void initControllers() {
        fileMenuController = new FileMenuController(window, this, microServerProxy);
        editMenuController = new EditMenuController(window, this, microServerProxy);
        adminMenuController = new AdminMenuController(window, this, microServerProxy);
    }

    private void initComponents() {

        chatPanel = new ChatPanel();
        chatPanelController = new ChatPanelController(this , chatPanel, microServerProxy);

        window.setPnlGameView(gameView);
        window.setChatPanel(chatPanel);

        window.initComponents();

        window.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                fileMenuController.storeRecents();
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        window.getLblRoomName().setEditable(false);
    }

    private void registerReceivers() {
        connectionController.registerReceiver(SetCurrentUserDTO.class, (ProtocolReceiver<SetCurrentUserDTO>) dto -> setCurrentUser(dto.getUser()));
        connectionController.registerReceiver(ErrorDTO.class, (ProtocolReceiver<ErrorDTO>) dto -> serverError(dto));
    }

    private void serverError(ErrorDTO dto) {
        String text = "Function: " + dto.getFunction() + "\nMessage:" + dto.getErrorMessage();
        JOptionPane.showMessageDialog(window,text, "Server Error", JOptionPane.ERROR_MESSAGE);
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
