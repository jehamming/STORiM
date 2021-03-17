package com.hamming.storim.model.dto.protocol.thing;


import com.hamming.storim.model.dto.ThingDto;
import com.hamming.storim.model.dto.protocol.ProtocolDTO;

public class GetThingResultDTO implements ProtocolDTO {

    private boolean success = false;
    private String errorMessage;
    private ThingDto thing;

    public GetThingResultDTO(boolean success, String errorMessage, ThingDto avatar) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.thing = avatar;
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
        return "GetThingResultDTO{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", thing=" + thing +
                '}';
    }
}
