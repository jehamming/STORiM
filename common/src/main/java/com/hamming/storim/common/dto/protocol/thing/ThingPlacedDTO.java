package com.hamming.storim.common.dto.protocol.thing;

import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ProtocolResponseDTO;

public class ThingPlacedDTO extends ProtocolResponseDTO {


    private ThingDto thing;
    private Long userId;

    public ThingPlacedDTO(Long userId, ThingDto thing) {
        this.thing = thing;
        this.userId = userId;
    }

    public ThingDto getThing() {
        return thing;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "ThingPlacedDTO{" +
                "thing=" + thing +
                ", userId=" + userId +
                '}';
    }
}