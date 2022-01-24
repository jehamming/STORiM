package com.hamming.storim.server;

import com.hamming.storim.common.dto.protocol.request.ClientTypeDTO;
import com.hamming.storim.common.net.Server;
import com.hamming.storim.common.net.ServerConfig;
import com.hamming.storim.server.common.dto.protocol.loginserver.AddServerRequestDTO;
import com.hamming.storim.server.common.dto.protocol.loginserver.AddServerResponseDTO;
import com.hamming.storim.server.common.factories.AvatarFactory;
import com.hamming.storim.server.common.factories.ThingFactory;
import com.hamming.storim.server.common.factories.TileFactory;
import com.hamming.storim.server.game.GameController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;

//
// This is a MicroServer
// CLients connect to this server to navigate in Rooms
//
public class STORIMMicroServer extends Server {

    private int port = 3131;
    private GameController controller;
    private int clients = 0;
    private ServerConfig config;
    private LoginServerConnection loginServerConnection;
    private UserDataServerConnection dataServerConnection;
    private final static String PROPFILE = "microserver.properties";
    public final static String DBFILE = "microserver.db";
    public static String DATADIR = "serverdata";
    public static String SERVERNAME = "servername";

    public STORIMMicroServer() {
        super("STORIM Micro Server");
    }

    public void initialize() {
        // Load Data
        Database.getInstance(DBFILE);
        // Load Config
        config = ServerConfig.getInstance(PROPFILE);
        // Set data variables
        DATADIR = config.getPropertyAsString("datadir");
        SERVERNAME = config.getPropertyAsString("name");
        //Force Tile loading
        TileFactory.getInstance(DATADIR);
        //Force Avatar Loading
        AvatarFactory.getInstance(DATADIR);
        //Force Thing Loading
        ThingFactory.getInstance(DATADIR);
        port = config.getPropertyAsInt("serverport");

        // Start GameController
        controller = new GameController();
        Thread controllerThread = new Thread(controller);
        controllerThread.setDaemon(true);
        controllerThread.setName("GameController");
        controllerThread.start();
        System.out.println(this.getClass().getName() + ":" + "GameController started");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> Database.getInstance().store()));
    }

    public void connectToLoginServer() {
        String loginservername = config.getPropertyAsString("loginserver");
        int loginserverport = config.getPropertyAsInt("loginserverport");
        try {
            Socket socket = new Socket(loginservername, loginserverport);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ClientTypeDTO clientTypeDTO = new ClientTypeDTO(SERVERNAME, ClientTypeDTO.TYPE_SERVER);
            out.writeObject(clientTypeDTO);
            loginServerConnection = new LoginServerConnection(clientTypeDTO, socket, in, out, controller);
            Thread controllerThread = new Thread(loginServerConnection);
            controllerThread.setDaemon(true);
            controllerThread.setName("LoginServerConnection");
            controllerThread.start();
            System.out.println(this.getClass().getName() + ":" + "Connected to LoginServer");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LoginServerConnection getLoginServerConnection() {
        return loginServerConnection;
    }

    public void connectToDataServer() {
        String dataservername = config.getPropertyAsString("userdataserver");
        int dataserverport = config.getPropertyAsInt("userdataserverport");
        try {
            Socket socket = new Socket(dataservername, dataserverport);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ClientTypeDTO clientTypeDTO = new ClientTypeDTO(SERVERNAME, ClientTypeDTO.TYPE_SERVER);
            out.writeObject(clientTypeDTO);
            dataServerConnection = new UserDataServerConnection(clientTypeDTO, socket, in, out, controller);
            Thread controllerThread = new Thread(dataServerConnection);
            controllerThread.setDaemon(true);
            controllerThread.setName("UserDataServerConnection");
            controllerThread.start();
            System.out.println(this.getClass().getName() + ":" + "Connected to DataServer");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void startServer() {
        boolean success = false;
        startServer(port);
        connectToDataServer();
        connectToLoginServer();
        success = registerToLoginServer();
        if (success) {
            System.out.println(this.getClass().getName() + ":" + "Started MicroServer: " + SERVERNAME + ", port:" + port);
        } else {
            System.out.println(this.getClass().getName() + ":" + "Could not start MicroServer: " + SERVERNAME + ", port:" + port);
            dispose();
        }
    }

    private boolean registerToLoginServer() {
        boolean success = false;
        try {
            String url = Inet4Address.getLocalHost().getHostName();
            AddServerRequestDTO dto = new AddServerRequestDTO(SERVERNAME, url, port);
            AddServerResponseDTO responseDTO = (AddServerResponseDTO) loginServerConnection.sendReceive(dto);
            success = responseDTO.isSuccess();
            if (success) {
                System.out.println(this.getClass().getName() + ":" + "Registered to LoginServer: " + responseDTO.getErrorMessage());
            } else {
                System.out.println(this.getClass().getName() + ":" + "Could not register to LoginServer: " + responseDTO.getErrorMessage());
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return success;
    }


    @Override
    protected void clientConnected(Socket s, ObjectInputStream in, ObjectOutputStream out) {
        try {
            clients++;
            ClientTypeDTO clientTypeDTO = (ClientTypeDTO) in.readObject();
            STORIMClientConnection client = new STORIMClientConnection(this, clientTypeDTO, s, in, out, controller);
            Thread clientThread = new Thread(client);
            controller.addListener(client);
            clientThread.setDaemon(true);
            String name = clientTypeDTO.getName() + "-" + clients;
            clientThread.setName(name);
            clientThread.start();
            System.out.println(this.getClass().getName() + ":" + "Client " + s.getInetAddress().toString() + ", ClientThread started");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        STORIMMicroServer server = new STORIMMicroServer();
        server.initialize();
        server.startServer();
    }
}
