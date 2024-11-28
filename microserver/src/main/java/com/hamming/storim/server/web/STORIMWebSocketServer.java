package com.hamming.storim.server.web;


import com.hamming.storim.common.net.WebsocketNetClient;
import com.hamming.storim.common.util.Logger;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMMicroServer;
import com.hamming.storim.server.engine.GameController;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;


public class STORIMWebSocketServer extends WebSocketServer {

    private int port;
    private final STORIMMicroServer storimMicroServer;
    private final GameController gameController;
    private final Map<WebSocket, WebsocketNetClient> connections;
    private long nbrOfClients;

    public STORIMWebSocketServer(STORIMMicroServer server, GameController controller, int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
        this.port = port;
        this.storimMicroServer = server;
        this.gameController = controller;
        this.connections = new HashMap<>();
        this.nbrOfClients = 0;
    }


    @Override
    public void start() {
        super.start();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Logger.info(this, "New websocket connection: " + handshake);
        String id = "WebsocketClient-" + nbrOfClients++;
        STORIMClientConnection storimClientConnection = new STORIMClientConnection(storimMicroServer, id, gameController);
        WebsocketNetClient websocketNetClient = new WebsocketNetClient(storimClientConnection, conn, storimClientConnection, storimClientConnection);
        storimClientConnection.setNetClient(websocketNetClient);
        websocketNetClient.connected();
        connections.put(conn, websocketNetClient);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        WebsocketNetClient clientConnection = connections.get(conn);
        if ( clientConnection != null ) {
            clientConnection.disconnected();
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        WebsocketNetClient clientConnection = connections.get(conn);
        if ( clientConnection != null ) {
            clientConnection.onMessage(message);
        }
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        WebsocketNetClient clientConnection = connections.get(conn);
        if ( clientConnection != null ) {
            clientConnection.onMessage(message);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
            WebsocketNetClient clientConnection = connections.get(conn);
            if ( clientConnection != null ) {
                clientConnection.disconnected();
            }
        }
    }

    @Override
    public void onStart() {
        Logger.info(this, "Started STORIM Websocket Server, listening on port:" + port) ;
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }

}