package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class ThingAddedDTO extends ResponseDTO {


    private ThingDto thing;

    public ThingAddedDTO(ThingDto thing) {
        super(true, null);
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
