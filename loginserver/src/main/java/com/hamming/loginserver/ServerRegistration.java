package com.hamming.loginserver;

public class ServerRegistration {

    private String serverName;
    private String serverURL;
    private int serverPort;

    public ServerRegistration(String name, String url, int port) {
        this.serverName = name;
        this.serverURL = url;
        this.serverPort = port;
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

    @Override
    public String toString() {
        return "ServerRegistration{" +
                "serverName='" + serverName + '\'' +
                ", serverURL='" + serverURL + '\'' +
                ", serverPort=" + serverPort +
                '}';
    }
}
