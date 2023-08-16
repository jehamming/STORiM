package com.hamming.storim.server.common.dto.protocol.dataserver.verb;

import com.hamming.storim.common.dto.VerbDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class DeleteVerbResponseDTO extends ResponseDTO {


    public DeleteVerbResponseDTO(boolean success, String errorMessage) {
        super(success, errorMessage);
    }


    @Override
    public String toString() {
        return "DeleteVerbResponseDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                '}';
    }
}
