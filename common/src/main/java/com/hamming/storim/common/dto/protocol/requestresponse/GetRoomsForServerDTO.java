package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetRoomsForServerDTO extends ProtocolDTO {
    private String serverName;
    public GetRoomsForServerDTO(String serverName) {
        this.serverName = serverName;
    }

    public String getServerName() {
        return serverName;
    }

    @Override
    public String toString() {
        return "GetRoomsForServerDTO{" +
                "serverName='" + serverName + '\'' +
                '}';
    }
}
