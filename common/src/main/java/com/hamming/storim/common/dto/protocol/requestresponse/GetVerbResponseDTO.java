package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.VerbDetailsDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetVerbResponseDTO implements ResponseDTO {

    private VerbDetailsDTO verb;

    public GetVerbResponseDTO(VerbDetailsDTO verb) {
        this.verb = verb;
    }

    public VerbDetailsDTO getVerb() {
        return verb;
    }

    @Override
    public String toString() {
        return "GetVerbRequestDTO{" +
                "verb=" + verb +
                '}';
    }
}
