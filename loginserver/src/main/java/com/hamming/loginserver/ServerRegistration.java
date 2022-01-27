package com.hamming.loginserver;

import com.hamming.storim.server.common.ClientConnection;
import com.sun.security.ntlm.Client;

public class ServerRegistration {

    private ClientConnection connection;
    private String serverName;
    private String serverURL;
    private int serverPort;

    public ServerRegistration(ClientConnection connection, String name, String url, int port) {
        this.serverName = name;
        this.serverURL = url;
        this.serverPort = port;
        this.connection = connection;
    }

    public String getServerName() {
        return serverName;
    }

    public String getServerURL() {
        return serverURL;
    }

    public int getServerPort() {
        return serverPort;
    }


    public ClientConnection getConnection() {
        return connection;
    }

    @Override
    public String toString() {
        return "ServerRegistration{" +
                "serverName='" + serverName + '\'' +
                ", serverURL='" + serverURL + '\'' +
                ", serverPort=" + serverPort +
                '}';
    }
}
