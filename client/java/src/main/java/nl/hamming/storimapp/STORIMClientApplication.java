package nl.hamming.storimapp;        // Moveement

import com.hamming.storim.Controllers;
import com.hamming.storim.controllers.*;
import com.hamming.storim.game.ProtocolHandler;
import nl.hamming.storimapp.view.ViewController;

public class STORIMClientApplication  {
    private Controllers controllers;
    private ProtocolHandler protocolHandler;
    private static STORIMClientApplication instance;
    private STORIMWindow STORIMWindow;
    private ViewController viewController;

    private STORIMClientApplication() {
        this.protocolHandler = new ProtocolHandler();
        initControllers();
    }

    public static STORIMClientApplication getInstance() {
        if (instance == null ) {
            instance = new STORIMClientApplication();
        }
        return instance;
    }

    public void initControllers() {
        controllers = new Controllers();
        // Connection
        controllers.setConnectionController(new ConnectionController());
        // Users
        controllers.setUserController( new UserController(controllers.getConnectionController()));
        // Verbs
        controllers.setVerbController(new VerbController(controllers));
        // Rooms
        controllers.setRoomController(new RoomController(controllers));
    }


    private void createAndShowGUI() {
        STORIMWindow = new STORIMWindow(controllers);
        STORIMWindow.getGameView().start();
        initControllers();

    }

    public static void main(String[] args) {
        final STORIMClientApplication application = new STORIMClientApplication();
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                application.createAndShowGUI();
            }
        });
    }

}
