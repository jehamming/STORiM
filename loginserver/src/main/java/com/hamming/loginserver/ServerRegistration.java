package com.hamming.loginserver;

public class ServerRegistration {

    private LoginServerClientConnection connection;
    private String serverName;
    private String serverURL;
    private int serverPort;
    private int hashcode;

    public ServerRegistration(LoginServerClientConnection connection, int hashcode, String name, String url, int port) {
        this.serverName = name;
        this.serverURL = url;
        this.serverPort = port;
        this.hashcode = hashcode;
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

    public int getHashcode() {
        return hashcode;
    }

    public LoginServerClientConnection getConnection() {
        return connection;
    }

    @Override
    public String toString() {
        return "ServerRegistration{" +
                "serverName='" + serverName + '\'' +
                ", serverURL='" + serverURL + '\'' +
                ", serverPort=" + serverPort +
                ", hashcode=" + hashcode +
                '}';
    }
}
