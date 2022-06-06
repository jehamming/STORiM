package com.hamming.storim.server.common.dto.protocol.dataserver.tile;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetThingRequestDTO implements ProtocolDTO {
    private Long thingID;
    public GetThingRequestDTO(Long thingID) {
        this.thingID = thingID;
    }

    public Long getThingID() {
        return thingID;
    }

    @Override
    public String toString() {
        return "GetThingRequestDTO{" +
                "thingID=" + thingID +
                '}';
    }
}
