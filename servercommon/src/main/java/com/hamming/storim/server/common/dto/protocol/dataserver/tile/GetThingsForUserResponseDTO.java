package com.hamming.storim.server.common.dto.protocol.dataserver.tile;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.util.List;

public class GetThingsForUserResponseDTO extends ResponseDTO {

    private boolean success = false;
    private String errorMessage;
    private final List<Long> things;

    public GetThingsForUserResponseDTO(boolean success, String errorMessage, List<Long> things) {
        this.things = things;
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public List<Long> getThings() {
        return things;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "GetThingsForUserResponseDTO{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", things=" + things +
                '}';
    }
}
