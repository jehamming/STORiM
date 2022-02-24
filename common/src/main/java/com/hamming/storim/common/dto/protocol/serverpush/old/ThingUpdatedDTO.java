package com.hamming.storim.common.dto.protocol.serverpush.old;

import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class ThingUpdatedDTO implements  ResponseDTO {


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
