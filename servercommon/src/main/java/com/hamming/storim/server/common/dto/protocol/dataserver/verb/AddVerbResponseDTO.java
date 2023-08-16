package com.hamming.storim.server.common.dto.protocol.dataserver.verb;

import com.hamming.storim.common.dto.VerbDetailsDTO;
import com.hamming.storim.common.dto.VerbDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class AddVerbResponseDTO extends ResponseDTO {

    private VerbDto verb;

    public AddVerbResponseDTO(boolean success, String errorMessage, VerbDto verb) {
        super(success, errorMessage);
        this.verb = verb;
    }

    public VerbDto getVerb() {
        return verb;
    }

    @Override
    public String toString() {
        return "AddVerbResponseDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", verb=" + verb +
                '}';
    }
}
