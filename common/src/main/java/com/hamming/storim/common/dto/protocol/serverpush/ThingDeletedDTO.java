package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class ThingDeletedDTO extends ResponseDTO {

    private Long thingId;

    public ThingDeletedDTO(Long thingId) {
        super(true, null);
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
