package com.hamming.storim.controllers;

import com.hamming.storim.game.ProtocolHandler;
import com.hamming.storim.interfaces.ViewerController;
import com.hamming.storim.model.dto.protocol.MovementRequestDTO;

public class MovementSender implements Runnable {

    private ConnectionController connectionController;
    private ProtocolHandler protocolHandler;
    private ViewerController viewerController;
    boolean running = false;
    private static int INTERVAL = 50; // Milliseconds 20hz

    public MovementSender(ViewerController viewerController, ConnectionController connectionController) {
        this.connectionController = connectionController;
        this.protocolHandler = new ProtocolHandler();
        this.viewerController = viewerController;
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            if (connectionController.isConnected()) {
                MovementRequestDTO data = viewerController.getCurrentMoveRequest();
                if (data != null) {
                    connectionController.send(data);
                }
            }
            try {
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running = false;
    }
}
