package com.hamming.storim.common.dto.protocol.thing;

import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ProtocolResponseDTO;

public class ThingUpdatedDTO extends ProtocolResponseDTO {


    private ThingDto thing;

    public ThingUpdatedDTO(ThingDto thing) {
        this.thing = thing;
    }

    public ThingDto getThing() {
        return thing;
    }

    @Override
    public String toString() {
        return "ThingUpdatedDTO{" +
                "thing=" + thing +
                '}';
    }
}
