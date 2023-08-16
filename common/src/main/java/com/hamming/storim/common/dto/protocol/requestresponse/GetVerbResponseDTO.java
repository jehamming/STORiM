package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.VerbDetailsDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetVerbResponseDTO extends ResponseDTO {

    private VerbDetailsDTO verb;

    public GetVerbResponseDTO(boolean success, String errormessage, VerbDetailsDTO verb) {
        super(success, errormessage);
        this.verb = verb;
    }

    public VerbDetailsDTO getVerb() {
        return verb;
    }

    @Override
    public String toString() {
        return "GetVerbResponseDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", verb=" + verb +
                '}';
    }
}
