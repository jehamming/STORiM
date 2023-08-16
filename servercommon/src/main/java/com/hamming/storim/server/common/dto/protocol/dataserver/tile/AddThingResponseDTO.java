package com.hamming.storim.server.common.dto.protocol.dataserver.tile;

import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class AddThingResponseDTO extends ResponseDTO {

    private ThingDto thing;

    public AddThingResponseDTO(boolean success, String errorMessage, ThingDto thing) {
        super(success, errorMessage);
        this.thing = thing;
    }


    public ThingDto getThing() {
        return thing;
    }

    @Override
    public String toString() {
        return "AddThingResponseDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", thing=" + thing +
                '}';
    }
}
