package com.hamming.storim;


import java.io.*;
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
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                clientConnected(s, in, out);
            } catch (SocketException e) {  //Do nothing
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

    public String getIp() {
        try {
            ss.getInetAddress();
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }


    protected abstract void clientConnected(Socket s, ObjectInputStream in, ObjectOutputStream out);

}