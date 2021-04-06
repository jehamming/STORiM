package com.hamming.storim.common.dto.protocol.thing;

import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class ThingAddedDTO implements ProtocolDTO {


    private ThingDto thing;

    public ThingAddedDTO(ThingDto thing) {
        this.thing = thing;
    }

    public ThingDto getThing() {
        return thing;
    }

    @Override
    public String toString() {
        return "ThingAddedDTO{" +
                "thing=" + thing +
                '}';
    }
}
