package com.hamming.storim.client;

import com.hamming.storim.common.controllers.ConnectionController;

public class STORIMClientApplication {

    private static String username = "";
    private static String password = "";



    private void createAndShowGUI() {
        STORIMWindow window = new STORIMWindow();
        STORIMWindowController windowController = new STORIMWindowController(window, username, password);
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
