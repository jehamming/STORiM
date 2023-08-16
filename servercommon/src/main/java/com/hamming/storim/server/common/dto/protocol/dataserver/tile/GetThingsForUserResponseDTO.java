package com.hamming.storim.server.common.dto.protocol.dataserver.tile;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.util.List;

public class GetThingsForUserResponseDTO extends ResponseDTO {

    private final List<Long> things;

    public GetThingsForUserResponseDTO(boolean success, String errorMessage, List<Long> things) {
        super(success, errorMessage);
        this.things = things;
    }

    public List<Long> getThings() {
        return things;
    }

    @Override
    public String toString() {
        return "GetThingsForUserResponseDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", things=" + things +
                '}';
    }
}
