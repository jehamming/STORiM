package com.hamming.storim.server.common.dto.protocol.dataserver.verb;

import com.hamming.storim.common.dto.VerbDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class UpdateVerbResponseDTO implements ResponseDTO {

    private boolean success;
    private String errorMessage;
    private VerbDto verb;

    public UpdateVerbResponseDTO(boolean success, String errorMessage, VerbDto verb) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.verb = verb;
    }

    public boolean isSuccess() {
        return success;
    }

    public VerbDto getVerb() {
        return verb;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "UpdateVerbResponseDTO{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", verb=" + verb +
                '}';
    }
}
