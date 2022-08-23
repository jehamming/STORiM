package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetThingDTO extends ProtocolDTO {

    private Long thingID;

    public GetThingDTO(Long thingID) {
        this.thingID = thingID;
    }

    public Long getThingID() {
        return thingID;
    }

    @Override
    public String toString() {
        return "GetThingDTO{" +
                "thingID=" + thingID +
                '}';
    }
}
