package com.hamming.storim.common.dto.protocol.thing;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class DeleteThingDTO implements ProtocolDTO {

    private Long thingId;

    public DeleteThingDTO(Long thingId) {
        this.thingId = thingId;
    }

    public Long getThingId() {
        return thingId;
    }

    @Override
    public String toString() {
        return "DeleteThingDTO{" +
                "thingId=" + thingId +
                '}';
    }
}
