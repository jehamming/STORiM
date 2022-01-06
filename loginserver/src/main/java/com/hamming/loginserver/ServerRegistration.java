package com.hamming.loginserver;

public class ServerRegistration {

    private String serverName;
    private String serverURL;
    private int serverPort;
    private int hashcode;

    public ServerRegistration(int hashcode, String name, String url, int port) {
        this.serverName = name;
        this.serverURL = url;
        this.serverPort = port;
        this.hashcode = hashcode;
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
