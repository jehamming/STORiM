package com.hamming.storim.server.common.dto.protocol.dataserver.avatar;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class UpdateThingResponseDTO extends ResponseDTO {

    private ThingDto thing;

    public UpdateThingResponseDTO(boolean success, String errorMessage, ThingDto thing) {
        super(success, errorMessage);
        this.thing = thing;
    }

    public ThingDto getThing() {
        return thing;
    }


    @Override
    public String toString() {
        return "UpdateThingResponseDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", thing=" + thing +
                '}';
    }
}
