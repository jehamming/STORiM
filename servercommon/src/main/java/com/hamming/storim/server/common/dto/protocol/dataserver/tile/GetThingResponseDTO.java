package com.hamming.storim.server.common.dto.protocol.dataserver.tile;

import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetThingResponseDTO extends ResponseDTO {

    private boolean success = false;
    private String errorMessage;
    private ThingDto thing;

    public GetThingResponseDTO(boolean success, String errorMessage, ThingDto thing) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.thing = thing;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ThingDto getThing() {
        return thing;
    }

    @Override
    public String toString() {
        return "GetThingResponseDTO{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", thing=" + thing +
                '}';
    }
}
