package com.hamming.storim.server.common.dto.protocol.dataserver.verb;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class DeleteThingRequestDto implements ProtocolDTO {

    private Long thingID;

    public DeleteThingRequestDto(Long thingID){
        this.thingID = thingID;
    }

    public Long getThingID() {
        return thingID;
    }

    @Override
    public String toString() {
        return "DeleteThingRequestDto{" +
                "thingID=" + thingID +
                '}';
    }
}
