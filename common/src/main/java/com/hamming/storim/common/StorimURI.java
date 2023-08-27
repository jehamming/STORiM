package com.hamming.storim.common;

import java.net.URI;
import java.net.URISyntaxException;

public class StorimURI {

    private URI serverURI;
    private String serverip;
    private int port;
    private Long roomId;
    private String serverURL;

    public StorimURI(String serverURLTxt) {
        try {
            serverURL = serverURLTxt;
            serverURI= new URI(serverURLTxt);
            serverip = serverURI.getHost();
            port = serverURI.getPort();
            roomId = null;
            try {
                if ( serverURI.getPath().length() > 1 ) {
                    roomId = Long.parseLong(serverURI.getPath().substring(1));
                }
            } catch (NumberFormatException ex) {
                // Do nothing
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public String getServerURL() {
        return serverURL;
    }

    public String getServerip() {
        return serverip;
    }

    public int getPort() {
        return port;
    }

    public Long getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return "StorimURI{" +
                ", serverip='" + serverip + '\'' +
                ", port=" + port +
                ", roomId=" + roomId +
                '}';
    }
}
