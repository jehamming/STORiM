package com.hamming.storim.server.common.dto.protocol.dataserver.verb;

import com.hamming.storim.common.dto.VerbDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class UpdateVerbResponseDTO extends ResponseDTO {

    private VerbDto verb;

    public UpdateVerbResponseDTO(boolean success, String errorMessage, VerbDto verb) {
        super(success, errorMessage);
        this.verb = verb;
    }

    public VerbDto getVerb() {
        return verb;
    }


    @Override
    public String toString() {
        return "UpdateVerbResponseDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", verb=" + verb +
                '}';
    }
}
