package com.hamming.loginserver;

import com.hamming.storim.common.net.ServerConfig;

import java.io.IOException;
import java.net.Socket;

// This is the Login Server for storim.
// CLients connect to this server and login
public class STORIMLoginServer implements Runnable {

    private ServerConfig config;
    private final static String PROPFILE = "loginserver.properties";
    private UserDataServerConnection userDataServerConnection;
    private LoginServerWorker serverWorker;
    private SessionManager sessionManager;
    private ServerConnectionServer serverConnectionServer;
    private ClientConnectionServer clientConnectionServer;
    private boolean running = false;

    public void initialize() {
        // Load Config
        config = ServerConfig.getInstance(PROPFILE);
        sessionManager = new SessionManager();

        // Start Worker
        serverWorker = new LoginServerWorker(this);
        Thread controllerThread = new Thread(serverWorker);
        controllerThread.setDaemon(true);
        controllerThread.setName("Login Server Worker");
        controllerThread.start();
        System.out.println(this.getClass().getName() + ":" + " Login Server Worker started");

        // Connecto to User Data Server
        connectToUserDataServer();

        serverConnectionServer = new ServerConnectionServer(this);
        clientConnectionServer = new ClientConnectionServer(this);
    }

    private void connectToUserDataServer() {
        // Connect to UserDataServer
        System.out.println(this.getClass().getName() + ":" + " Trying to connect to the UserDataServer...");
        String dataservername = config.getPropertyAsString("userdataserver");
        int dataserverport = config.getPropertyAsInt("userdataserverport");
        try {
            Socket socket = new Socket(dataservername, dataserverport);
            userDataServerConnection = new UserDataServerConnection(getClass().getSimpleName(), socket, serverWorker);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(this.getClass().getName() + ":" + " UserDataServerConnection started");
    }



    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public UserDataServerConnection getUserDataServerConnection() {
        return userDataServerConnection;
    }

    public LoginServerWorker getServerWorker() {
        return serverWorker;
    }

    public ServerConfig getConfig() {
        return config;
    }

    @Override
    public void run() {
        initialize();
        // Start listening for servers and clients
        serverConnectionServer.startServer();
        clientConnectionServer.startServer();
        running = true;
        System.out.println(this.getClass().getName() + ":" + "Started STORIM LOGIN Server" );
        while (running) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(this.getClass().getName() + ":" + "STORIM LOGIN Servers stopped");
    }

    public void stopServer() {
        running = false;
    }

    public static void main(String[] args) {
        STORIMLoginServer server = new STORIMLoginServer();
        Thread t = new Thread(server);
        t.start();
    }

}
