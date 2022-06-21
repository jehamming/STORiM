package com.hamming.storim.client;

import com.hamming.storim.common.controllers.ConnectionController;

public class STORIMClientApplication {
    private STORIMWindow STORIMWindow;
    private ConnectionController connectionController;
    private static String username = "";
    private static String password = "";

    private STORIMClientApplication() {
        initControllers();
    }


    public void initControllers() {
        connectionController = new ConnectionController("STORIM_Java_client");
    }


    private void createAndShowGUI() {
        STORIMWindow = new STORIMWindow(connectionController, username, password);
        STORIMWindow.setVisible(true);
        STORIMWindow.getGameView().start();
        initControllers();

    }

    public static void main(String[] args) {
        final STORIMClientApplication application = new STORIMClientApplication();
        if (args.length == 2 ) {
            username = args[0];
            password = args[1];
        }
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(() -> application.createAndShowGUI());
    }

}
