package com.hamming.storim.server.common.dto.protocol.dataserver.verb;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class DeleteThingResponseDTO extends ResponseDTO {


    public DeleteThingResponseDTO(boolean success, String errorMessage) {
        super(success, errorMessage);
    }

    @Override
    public String toString() {
        return "DeleteThingResponseDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                '}';
    }
}
