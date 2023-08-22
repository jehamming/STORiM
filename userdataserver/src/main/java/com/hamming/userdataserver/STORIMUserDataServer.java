package com.hamming.userdataserver;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.net.Server;
import com.hamming.storim.common.net.ServerConfig;
import com.hamming.storim.common.util.Logger;
import com.hamming.storim.server.Database;
import com.hamming.storim.server.STORIMException;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.NetUtils;
import com.hamming.userdataserver.factories.AvatarFactory;
import com.hamming.userdataserver.factories.ThingFactory;
import com.hamming.userdataserver.factories.TileFactory;
import com.hamming.userdataserver.factories.UserFactory;
import com.hamming.userdataserver.model.User;

import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

// This is the UserData Server for storim.
// MicroServers & LoginServer connect to retrieve data
public class STORIMUserDataServer extends Server {

    private int port = 3131;
    private int clients = 0;
    private ServerWorker serverWorker;

    private SessionManager sessionManager;

    private final static String PROPFILE = "userdataserver.properties";
    private final static String PROP_DATADIR= "datadir";
    private final static String PROP_SERVERPORT= "serverport";
    private final static String PROP_ADMINS= "admins";
    public final static String DBFILE = "userdata.db";
    public static String DATADIR = "data";

    private ServerConfig config;
    private List<User> admins;

    public STORIMUserDataServer() {
        super("STORIM User Data Server");
    }

    public void initialize() {
        // Load Config
        config = ServerConfig.getInstance(PROPFILE);
        // Set datadir var
        DATADIR = config.getPropertyAsString(PROP_DATADIR);
        // Load Data
        Database.getInstance(DBFILE);
        checkDatabase();
        //Force Avatar Loading
        AvatarFactory.getInstance(DATADIR);
        //Force Thing Loading
        ThingFactory.getInstance(DATADIR);
        //Force Tile loading
        TileFactory.getInstance(DATADIR);
        setServerAdmins();

        port = config.getPropertyAsInt(PROP_SERVERPORT);

        sessionManager = new SessionManager();

        // Start Worker
        serverWorker = new ServerWorker();
        Thread controllerThread = new Thread(serverWorker);
        controllerThread.setDaemon(true);
        controllerThread.setName("UserData Server Worker");
        controllerThread.start();
        Logger.info(this, "UserData Server Worker started");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> Database.getInstance().store()) {});
    }

    private void setServerAdmins() {
        admins = new ArrayList<>();
        String adminsString = config.getPropertyAsString(PROP_ADMINS);
        if ( adminsString != null && !adminsString.equals("") ) {
            String[] adminUsernames = adminsString.split(",");
            for (int i = 0; i < adminUsernames.length ; i++) {
                String username = adminUsernames[i];
                User adminUser = UserFactory.getInstance().findUserByUsername(username);
                admins.add(adminUser);
            }
        }
    }

    private void checkDatabase() {
        UserFactory.getInstance().getRootUser();
        Database.getInstance().store();
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public void startServer() {
        startServer(port);
        Logger.info(this, "---- For property files -----");
        try {
            Logger.info(this, "userdataserver="+ Inet4Address.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Logger.info(this, "userdataserverport="+ port);
        Logger.info(this, "-----------------------------");
        try {
            //String shortURL = Inet4Address.getLocalHost().getHostAddress() + ":" + port;
            String shortURL = NetUtils.getLocalHostLANAddress() + ":" + port;
            Logger.info(this, "Started STORIM DATA Server on: "+shortURL);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void clientConnected(Socket s) {
        try {
            clients++;
            //FIXME Keep a record of all the clients?
            ClientConnection client = new UserDataClientConnection(this,UserDataClientConnection.class.getSimpleName(), s, serverWorker);
            Logger.info(this, "new connection, ClientThread started for "+ client.getId());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public List<User> getAdmins() {
        return admins;
    }

    public static void main(String[] args) {
        STORIMUserDataServer server = new STORIMUserDataServer();
        server.initialize();
        server.startServer();
    }


}
