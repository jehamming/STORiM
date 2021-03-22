package com.hamming.storim.model.dto.protocol.thing;

import com.hamming.storim.model.dto.ThingDto;
import com.hamming.storim.model.dto.protocol.ProtocolDTO;

public class ThingDeletedDTO implements ProtocolDTO {

    private ThingDto thing;

    public ThingDeletedDTO(ThingDto thing) {
        this.thing = thing;
    }

    public ThingDto getThing() {
        return thing;
    }

    @Override
    public String toString() {
        return "ThingDeletedDTO{" +
                "thing=" + thing +
                '}';
    }
}
