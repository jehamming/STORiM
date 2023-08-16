package com.hamming.storim.common.dto.protocol.requestresponse;


import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetThingResultDTO extends ResponseDTO {

    private ThingDto thing;

    public GetThingResultDTO(boolean success, String errorMessage, ThingDto avatar) {
        super(success, errorMessage);
        this.thing = avatar;
    }

    public ThingDto getThing() {
        return thing;
    }

    @Override
    public String toString() {
        return "GetThingResultDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", thing=" + thing +
                '}';
    }
}
