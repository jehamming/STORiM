package com.hamming.loginserver;

import com.hamming.storim.common.net.Server;
import com.hamming.storim.server.common.ClientConnection;

import java.net.Socket;

// This is the Login Server for storim.
// CLients connect to this server and login
public class ClientConnectionServer extends Server {

    private int connectedClients = 0;
    private int port = -1;
    private STORIMLoginServer loginServer;


    public ClientConnectionServer(STORIMLoginServer loginServer) {
        super("STORIM Server Connection Listener");
        this.loginServer = loginServer;
    }


    public void startServer() {
        port = loginServer.getConfig().getPropertyAsInt("listenport.client");
        startServer(port);
        System.out.println(this.getClass().getName() + ":" + "Listening for client connections on port:" + port);
    }

    @Override
    protected void clientConnected(Socket s) {
        connectedClients++;
        String name = "Client-" + s.getInetAddress().toString() +  "-" + connectedClients;
        //FIXME Keep a record of connected clients ?
        ClientConnection client = new UserClientConnection(name, s, loginServer.getServerWorker());
        System.out.println(this.getClass().getName() + ": Client " + name + " connected, ClientThread started");


    }





}
