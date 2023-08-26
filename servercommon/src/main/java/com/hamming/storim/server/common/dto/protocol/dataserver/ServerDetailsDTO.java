package com.hamming.storim.server.common.dto.protocol.dataserver;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class ServerDetailsDTO extends ProtocolDTO {

    private String serverId;

    public ServerDetailsDTO(String serverId) {
        this.serverId = serverId;
    }

    public String getServerId() {
        return serverId;
    }

    @Override
    public String toString() {
        return "ServerDetailsDTO{" +
                "serverId='" + serverId + '\'' +
                '}';
    }
}
