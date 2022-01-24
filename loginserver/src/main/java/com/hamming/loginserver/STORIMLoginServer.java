package com.hamming.loginserver;

import com.hamming.storim.common.dto.protocol.request.ClientTypeDTO;
import com.hamming.storim.common.net.Server;
import com.hamming.storim.common.net.ServerConfig;
import com.hamming.storim.server.common.ClientConnection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;

// This is the Login Server for storim.
// CLients connect to this server and login
public class STORIMLoginServer extends Server {

    private int port = 3131;
    private int connectedClients = 0;
    private int connectedServers = 0;
    private ServerConfig config;
    private final static String PROPFILE = "loginserver.properties";
    private UserDataServerConnection userDataServerConnection;
    private LoginServerWorker serverWorker;
    private SessionManager sessionManager;

    public STORIMLoginServer() {
        super("STORIM Login Server");
    }

    public void initialize() {
        // Load Config
        config = ServerConfig.getInstance(PROPFILE);
        port = config.getPropertyAsInt("serverport");

        sessionManager = new SessionManager();

        // Start Worker
        serverWorker = new LoginServerWorker(this);
        Thread controllerThread = new Thread(serverWorker);
        controllerThread.setDaemon(true);
        controllerThread.setName("Login Server Worker");
        controllerThread.start();
        System.out.println(this.getClass().getName() + ":" + " Login Server Worker started");

        connectToUserDataServer();
    }

    private void connectToUserDataServer() {
        // Connect to UserDataServer
        System.out.println(this.getClass().getName() + ":" + " Trying to connect to the UserDataServer...");
        String dataservername = config.getPropertyAsString("userdataserver");
        int dataserverport = config.getPropertyAsInt("userdataserverport");
        try {
            Socket socket = new Socket(dataservername, dataserverport);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ClientTypeDTO clientTypeDTO = new ClientTypeDTO(getClass().getSimpleName(), ClientTypeDTO.TYPE_SERVER);
            out.writeObject(clientTypeDTO);
            userDataServerConnection = new UserDataServerConnection(clientTypeDTO, socket, in, out, serverWorker);
            Thread controllerThread = new Thread(userDataServerConnection);
            controllerThread.setDaemon(true);
            controllerThread.setName("UserDataServerConnection");
            controllerThread.start();
            System.out.println(this.getClass().getName() + ":" + "Connected to DataServer");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(this.getClass().getName() + ":" + " UserDataServerConnection started");
    }


    public void startServer() {
        startServer(port);
        System.out.println("---- For property files -----");
        try {
            System.out.println("loginserver=" + Inet4Address.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println("loginserverport=" + port);
        System.out.println("-----------------------------");
        System.out.println(this.getClass().getName() + ":" + "Started STORIM LOGIN Server, port:" + port);
    }


    @Override
    protected void clientConnected(Socket s, ObjectInputStream in, ObjectOutputStream out) {
        try {
            // Check for a server or client connection
            ClientTypeDTO clientTypeDTO = (ClientTypeDTO) in.readObject();
            switch (clientTypeDTO.getType()) {
                case ClientTypeDTO.TYPE_SERVER:
                    newServerConnection(clientTypeDTO, s, in, out);
                    break;
                case ClientTypeDTO.TYPE_CLIENT:
                    newClientConnection(clientTypeDTO, s, in, out);
                    break;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void newClientConnection(ClientTypeDTO clientTypeDTO, Socket s, ObjectInputStream in, ObjectOutputStream out) {
        connectedClients++;
        ClientConnection client = new UserClientConnection(clientTypeDTO, s, in, out, serverWorker);
        Thread clientThread = new Thread(client);
        clientThread.setDaemon(true);
        clientThread.setName(clientTypeDTO.getName() + ":" + s.getInetAddress().toString());
        clientThread.start();
        System.out.println(this.getClass().getName() + ": Client " + clientTypeDTO.getName() + " connected, ClientThread started");
    }

    private void newServerConnection(ClientTypeDTO clientTypeDTO, Socket s, ObjectInputStream in, ObjectOutputStream out) {
        connectedServers++;
        String name = clientTypeDTO.getName() + "-" + connectedServers;
        ClientConnection client = new LoginServerClientConnection(clientTypeDTO, s, in, out, serverWorker);
        Thread clientThread = new Thread(client);
        clientThread.setDaemon(true);
        clientThread.setName(name + ":" + s.getInetAddress().toString());
        clientThread.start();
        System.out.println(this.getClass().getName() + ": Server " + clientThread.getName() + " connected, ClientThread started");
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public UserDataServerConnection getUserDataServerConnection() {
        return userDataServerConnection;
    }

    public static void main(String[] args) {
        STORIMLoginServer server = new STORIMLoginServer();
        server.initialize();
        server.startServer();
    }


}
