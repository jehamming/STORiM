package com.hamming.storim.common;

import java.net.URI;
import java.net.URISyntaxException;

public class StorimURI {

    private URI serverURI;
    private String serverip;
    private int port;
    private Long roomId;

    public StorimURI(String serverURLTxt) {
        try {
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

    public URI getServerURI() {
        return serverURI;
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
}
