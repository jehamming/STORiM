package com.hamming.storim;

import com.hamming.storim.factories.*;
import com.hamming.storim.game.GameController;

import java.io.BufferedReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

// This is the main Server for storim.
// CLients connect to this server and login
public class STORIMServer extends Server {

    private int port = 3131;
    private GameController controller;
    private int clients = 0;

    public STORIMServer() {
        super("STORIM");
    }

    public void initialize() {
        // Load Config
        ServerConfig config = ServerConfig.getInstance();
        //Force Tile loading
        TileFactory.getInstance();
        //Force Avatar Loading
        AvatarFactory.getInstance();
        port = config.getServerPort();
        // Load Data
        Database.getInstance();
        // Start GameController
        controller = new GameController();
        Thread controllerThread = new Thread(controller);
        controllerThread.setDaemon(true);
        controllerThread.setName("GameController");
        controllerThread.start();
        System.out.println(this.getClass().getName() + ":" + "GameController started");
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                Database.getInstance().store();
            }
        }) {});
    }


    public void startServer() {
        startServer(port);
        System.out.println(this.getClass().getName() + ":" + "Started main STORIM Server, port:"+port);
    }


    @Override
    protected void clientConnected(Socket s, ObjectInputStream in, ObjectOutputStream out) {
        try {
            clients++;
            ClientConnection client = new ClientConnection("Client-"+clients, s, in, out, controller);
            Thread clientThread = new Thread(client);
            controller.addListener(client);
            clientThread.setDaemon(true);
            clientThread.setName("Client " + s.getInetAddress().toString());
            clientThread.start();
            System.out.println(this.getClass().getName() + ":" + "Client " + s.getInetAddress().toString() + ", ClientThread started");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        STORIMServer server = new STORIMServer();
        server.initialize();
        server.startServer();
    }


}
