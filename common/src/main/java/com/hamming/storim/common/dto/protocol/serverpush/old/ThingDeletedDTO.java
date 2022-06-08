package com.hamming.storim.common.dto.protocol.serverpush.old;

import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class ThingDeletedDTO implements  ResponseDTO {

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
