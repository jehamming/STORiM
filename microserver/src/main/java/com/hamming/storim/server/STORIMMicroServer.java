package com.hamming.storim.server;

import com.hamming.storim.common.dto.TileSetDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.request.DeleteTileSetDTO;
import com.hamming.storim.common.net.Server;
import com.hamming.storim.common.net.ServerConfig;
import com.hamming.storim.common.util.Logger;
import com.hamming.storim.server.common.NetUtils;
import com.hamming.storim.server.common.factories.ExitFactory;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.factories.TileSetFactory;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.common.model.TileSet;
import com.hamming.storim.server.game.GameController;

import javax.imageio.ImageIO;
import javax.xml.crypto.Data;
import java.awt.*;
import java.io.File;
import java.io.IOException;
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
    public static String DEFAULT_MAINROOM_NAME = "Main Square";
    public static String DEFAULT_SERVERCONFIG_NAME = "ServerConfig";
    private ServerConfiguration serverConfiguration;

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
        // Load the ServeConfig
        getServerConfigFromDatabase();

        checkTileSets();
        checkRooms();

        connectToDataServer();
        setSuperAdmin();


        // Start GameController
        controller = new GameController();
        Thread controllerThread = new Thread(controller);
        controllerThread.setDaemon(true);
        controllerThread.setName("GameController");
        controllerThread.start();
        Logger.info(this, "GameController started");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> Database.getInstance().store()));
    }

    private void setSuperAdmin() {
        String serverAdmin = config.getPropertyAsString("admin");
        if ( serverAdmin != null ) {
            try {
               UserDto adminUser = userDataServerProxy.getUserByUsername(serverAdmin);
               serverConfiguration.setSuperAdmin(adminUser.getId());
            } catch (STORIMException e) {
                Logger.info(this,"setSuperAdmin:"+e.getMessage());
            }
        }
    }

    private void getServerConfigFromDatabase() {
        serverConfiguration = Database.getInstance().findByName(ServerConfiguration.class, DEFAULT_SERVERCONFIG_NAME );
        if ( serverConfiguration == null ) {
            // Not present, create one
            Long id = Database.getInstance().getNextID();
            serverConfiguration = new ServerConfiguration();
            serverConfiguration.setId(id);
            Long creatorId = 1L; //TODO Replace 1L with ROOT user..
            serverConfiguration.setOwnerId(creatorId);
            serverConfiguration.setName(DEFAULT_SERVERCONFIG_NAME);
            serverConfiguration.setServerName(serverName);
            Database.getInstance().addBasicObject(serverConfiguration);
        }
    }

    private void checkRooms() {
        if ( RoomFactory.getInstance().getRooms().size() == 0 ) {
            //No rooms ? Create a default room!
            Room mainRoom = addRoom(1L, DEFAULT_MAINROOM_NAME);
            serverConfiguration.setDefaultRoom(mainRoom);
            Database.getInstance().store();
            Logger.info(this, "No Rooms found, created default room:" + mainRoom.getName()+ "("+ mainRoom.getId() +")");
        }
    }

    private void checkTileSets() throws IOException {
        if ( TileSetFactory.getInstance().getAllTileSets().size() == 0 ) {
            Long creatorId = 1L; //TODO Replace 1L with ROOT user..
            Image defaultTileSetImage = ImageIO.read(new File("DEFAULT_TILESET.png"));
            TileSet defaultTileSet = TileSetFactory.getInstance().createTileSet(creatorId, TileSetFactory.DEFAULT_TILESET_NAME, defaultTileSetImage, 32, 32);
            serverConfiguration.setDefaultTileSet(defaultTileSet);
            serverConfiguration.setDefaultTile(0);
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

    public Room addRoom(Long creatorId, String name) {
        Room room = RoomFactory.getInstance().createRoom(creatorId, name);
        TileSet tileSet = serverConfiguration.getDefaultTileSet();
        int tileId = serverConfiguration.getDefaultTile();
        room.setBackTileSetId(tileSet.getId());
        room.setBackTileMap(fillTileMap(room.getRows(), room.getCols(), tileId));
        room.setOwnerId(creatorId);
        return room;
    }

    private int[][] fillTileMap(int rows, int cols, int value) {
        int[][] newTileMap = new int[cols][rows];
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                newTileMap[c][r] = value;
            }
        }
        return newTileMap;
    }

    public TileSet getDefaultTileSet() {
        return serverConfiguration.getDefaultTileSet();
    }

    public ServerConfiguration getServerConfiguration() {
        return serverConfiguration;
    }
}
