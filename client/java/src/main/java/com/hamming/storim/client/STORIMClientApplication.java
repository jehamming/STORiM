package com.hamming.storim.client;

import com.hamming.storim.common.Controllers;
import com.hamming.storim.common.ProtocolHandler;
import com.hamming.storim.common.controllers.*;
import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.interfaces.RoomListener;

public class STORIMClientApplication {
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
        controllers.setConnectionController(new ConnectionController("STORIM_Java_client"));
        // Users
        controllers.setUserController( new UserController(controllers.getConnectionController()));
        // Verbs
        controllers.setVerbController(new VerbController(controllers));
        // Rooms
        controllers.setRoomController(new RoomController(controllers));
        // Things
        controllers.setThingController(new ThingController(controllers));
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
