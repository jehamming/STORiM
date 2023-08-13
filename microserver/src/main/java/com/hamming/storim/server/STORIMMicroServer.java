package com.hamming.storim.server;

import com.hamming.storim.common.net.Server;
import com.hamming.storim.common.net.ServerConfig;
import com.hamming.storim.common.util.Logger;
import com.hamming.storim.server.common.NetUtils;
import com.hamming.storim.server.common.factories.ExitFactory;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.factories.TileSetFactory;
import com.hamming.storim.server.common.model.TileSet;
import com.hamming.storim.server.game.GameController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
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
    private UserDataServerConnection dataServerConnection;
    private UserDataServerProxy userDataServerProxy;
    private final static String PROPFILE = "microserver.properties";
    public final static String DBFILE = "microserver.db";
    public static String DATADIR = "serverdata";
    private String serverName = "servername";
    private String serverURI;

    public STORIMMicroServer() {
        super("STORIM Micro Server");
    }

    public String getServerName() {
        return serverName;
    }

    public String getServerURI() {
        return serverURI;
    }

    public void initialize() throws IOException {
        // Load Data
        Database.getInstance(DBFILE);
        // Load Config
        config = ServerConfig.getInstance(PROPFILE);
        // Set data variables
        DATADIR = config.getPropertyAsString("datadir");
        serverName = config.getPropertyAsString("name");
        port = config.getPropertyAsInt("serverport");

        ExitFactory.getInstance(DATADIR);
        TileSetFactory.getInstance(DATADIR);

        checkRooms();
        checkTileSets();

        // Start GameController
        controller = new GameController();
        Thread controllerThread = new Thread(controller);
        controllerThread.setDaemon(true);
        controllerThread.setName("GameController");
        controllerThread.start();
        Logger.info(this, "GameController started");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> Database.getInstance().store()));
    }

    private void checkRooms() {
        if ( RoomFactory.getInstance().getRooms().size() == 0 ) {
            //No rooms ? Create a default room!
            RoomFactory.getInstance().createRoom(1l, "Main square");
            Database.getInstance().store();
            Logger.info(this, "No Rooms found, created default room");
        }
    }

    private void checkTileSets() throws IOException {
        if ( TileSetFactory.getInstance().getAllTileSets().size() == 0 ) {
            Long creatorId = 1L; //TODO Replace 1L with ROOT user..
            // Default set 1
            Image image = ImageIO.read(new File("default_tileset1.png"));
            TileSetFactory.getInstance().createTileSet("Default_Set1", creatorId, image, 32, 32);
            // Default set 2
            Image image2 = ImageIO.read(new File("default_tileset2.png"));
            TileSetFactory.getInstance().createTileSet("Default_Set2", creatorId, image2, 16, 16);
            Logger.info(this, "No TileSets found, created 2 Default tilesets");
        }
    }


    public void connectToDataServer() {
        String dataservername = config.getPropertyAsString("userdataserver");
        int dataserverport = config.getPropertyAsInt("userdataserverport");
        try {
            Socket socket = new Socket(dataservername, dataserverport);
            dataServerConnection = new UserDataServerConnection(getClass().getSimpleName(), socket,  controller);
            userDataServerProxy = new UserDataServerProxy(dataServerConnection);
            Logger.info(this, "Connected to DataServer");
        } catch (IOException e) {
            e.printStackTrace();
            Logger.info(this, "Fatal Error, cannot connect to Data Server!");
            System.exit(-2);
        }
    }

    public UserDataServerProxy getUserDataServerProxy() {
        return userDataServerProxy;
    }

    public void startServer() {
        startServer(port);
        connectToDataServer();
        try {
            //serverURI = ServerConfig.PROTOCOL+"://" + Inet4Address..getLocalHost().getHostAddress() + ":" + port;
            serverURI = ServerConfig.PROTOCOL+":/" + NetUtils.getLocalHostLANAddress() + ":" + port;
            Logger.info(this, "Started STORIM Micro Server on: "+serverURI);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void clientConnected(Socket s) {
        try {
            clients++;
            String id = "client-"+clients;
            STORIMClientConnection client = new STORIMClientConnection(this, id, s,controller);
            Logger.info(this, "Client " + s.getInetAddress().toString() + ", ClientThread started");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        STORIMMicroServer server = new STORIMMicroServer();
        server.initialize();
        server.startServer();
    }
}
