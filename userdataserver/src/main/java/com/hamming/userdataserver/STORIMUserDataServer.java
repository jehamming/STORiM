package com.hamming.userdataserver;

import com.hamming.storim.common.net.Server;
import com.hamming.storim.common.net.ServerConfig;
import com.hamming.storim.server.Database;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.factories.AvatarFactory;
import com.hamming.storim.server.common.factories.ThingFactory;

import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;

// This is the UserData Server for storim.
// MicroServers & LoginServer connect to retrieve data
public class STORIMUserDataServer extends Server {

    private int port = 3131;
    private int clients = 0;
    private ServerWorker serverWorker;

    private final static String PROPFILE = "userdataserver.properties";
    public final static String DBFILE = "userdata.db";
    public static String DATADIR = "data";

    public STORIMUserDataServer() {
        super("STORIM User Data Server");
    }

    public void initialize() {
        // Load Config
        ServerConfig config = ServerConfig.getInstance(PROPFILE);
        // Set datadir var
        DATADIR = config.getPropertyAsString("datadir");
        // Load Data
        Database.getInstance(DBFILE);
        //Force Avatar Loading
        AvatarFactory.getInstance(DATADIR);
        //Force Thing Loading
        ThingFactory.getInstance(DATADIR);

        port = config.getPropertyAsInt("serverport");

        // Start Worker
        serverWorker = new ServerWorker();
        Thread controllerThread = new Thread(serverWorker);
        controllerThread.setDaemon(true);
        controllerThread.setName("UserData Server Worker");
        controllerThread.start();
        System.out.println(this.getClass().getName() + ":" + " UserData Server Worker started");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> Database.getInstance().store()) {});
    }


    public void startServer() {
        startServer(port);
        System.out.println("---- For property files -----");
        try {
            System.out.println("userdataserver=" + Inet4Address.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println("userdataserverport=" + port);
        System.out.println("-----------------------------");
        System.out.println(this.getClass().getName() + ":" + "Started STORIM DATA Server, open for client connections");
    }


    @Override
    protected void clientConnected(Socket s) {
        try {
            clients++;
            //FIXME Keep a record of all the clients?
            ClientConnection client = new UserDataClientConnection(null, s, serverWorker);
            System.out.println(this.getClass().getName() + ": new connection, ClientThread started");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        STORIMUserDataServer server = new STORIMUserDataServer();
        server.initialize();
        server.startServer();
    }


}
