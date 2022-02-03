package com.hamming.storim.common.net;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

// This is the main server class
public abstract class Server implements Runnable {
    private int port;
    private boolean running = true;
    private String name;
    private ServerSocket ss;

    public Server(String name) {
        this.name = name;
    }

    public void startServer(int port) {
        try {
            ss = new ServerSocket(port);
            if (this.port == 0) this.port = ss.getLocalPort();
            else this.port = port;
            Thread serverThread = new Thread(this);
            serverThread.setName(name + "Server");
            serverThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                Socket s = ss.accept();
                clientConnected(s);
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void dispose() {
        running = false;
        try {
            ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ss = null;
    }

    protected abstract void clientConnected(Socket s);

}