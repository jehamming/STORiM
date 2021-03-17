package com.hamming.storim.model.dto.protocol.thing;

import com.hamming.storim.model.dto.ThingDto;
import com.hamming.storim.model.dto.protocol.ProtocolDTO;

public class ThingUpdatedDTO implements ProtocolDTO {


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
