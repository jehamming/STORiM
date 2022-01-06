package com.hamming.storim.server.common.dto.protocol.loginserver;

import com.hamming.storim.common.dto.protocol.ProtocolASyncRequestDTO;

public class AddServerRequestDTO extends ProtocolASyncRequestDTO {

    private String name;
    private String url;
    private int port;

    public AddServerRequestDTO(String name, String url, int port) {
        this.name = name;
        this.url = url;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "AddServerDTO{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", port=" + port +
                '}';
    }
}
