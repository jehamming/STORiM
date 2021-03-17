package com.hamming.storim.model.dto.protocol.thing;

import com.hamming.storim.model.dto.protocol.ProtocolDTO;

public class ThingDeletedDTO implements ProtocolDTO {

    private Long thingId;

    public ThingDeletedDTO(Long thingId) {
        this.thingId = thingId;
    }

    public Long getThingId() {
        return thingId;
    }

    @Override
    public String toString() {
        return "ThingDeletedDTO{" +
                "thingId=" + thingId +
                '}';
    }
}
