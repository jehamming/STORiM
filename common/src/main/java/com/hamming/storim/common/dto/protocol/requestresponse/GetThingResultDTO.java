package com.hamming.storim.common.dto.protocol.requestresponse;


import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetThingResultDTO extends ResponseDTO {

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
