package com.hamming.storim.server.common.dto.protocol.dataserver.tile;

import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class AddThingResponseDTO implements ResponseDTO {

    private boolean success;
    private String errorMessage;
    private ThingDto thing;

    public AddThingResponseDTO(boolean success, String errorMessage, ThingDto thing) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.thing = thing;
    }

    public boolean isSuccess() {
        return success;
    }

    public ThingDto getThing() {
        return thing;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "AddThingResponseDTO{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", thing=" + thing +
                '}';
    }
}
