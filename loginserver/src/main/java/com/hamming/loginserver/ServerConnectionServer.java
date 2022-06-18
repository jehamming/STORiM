package com.hamming.loginserver;

import com.hamming.storim.common.net.Server;
import com.hamming.storim.common.util.Logger;
import com.hamming.storim.server.common.ClientConnection;

import java.net.Socket;

// This is the Login Server for storim.
// CLients connect to this server and login
public class ServerConnectionServer extends Server {

    private int port = -1;
    private int connectedServers = 0;
    private STORIMLoginServer loginServer;

    public ServerConnectionServer(STORIMLoginServer loginServer) {
        super("STORIM Server Connection Listener");
        this.loginServer = loginServer;
    }


    public void startServer() {
        port = loginServer.getConfig().getPropertyAsInt("listenport.server");
        startServer(port);
        Logger.info(this, "Listening for server connections on port:" + port);
    }


    @Override
    protected void clientConnected(Socket s) {
        connectedServers++;
        String name = "Server-" + s.getInetAddress().toString() +  "-" + connectedServers;
        //FIXME Keep a record of connected servers ?
        ClientConnection server = new LoginServerClientConnection(name, s, loginServer.getServerWorker());
        Logger.info(this, "Server " + name + " connected, ClientThread started");
    }


}
