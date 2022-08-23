package com.hamming.storim.server.common.dto.protocol.dataserver.avatar;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class UpdateThingResponseDTO extends ResponseDTO {

    private boolean success;
    private String errorMessage;
    private ThingDto thing;

    public UpdateThingResponseDTO(boolean success, String errorMessage, ThingDto thing) {
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
        return "UpdateThingResponseDTO{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", thing=" + thing +
                '}';
    }
}
