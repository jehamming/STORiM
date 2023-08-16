package com.hamming.storim.server.common.dto.protocol.dataserver.verb;

import com.hamming.storim.common.dto.VerbDetailsDTO;
import com.hamming.storim.common.dto.VerbDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetVerbDetailsResponseDTO extends ResponseDTO {

    private VerbDetailsDTO verb;

    public GetVerbDetailsResponseDTO(boolean success, String errorMessage, VerbDetailsDTO verb) {
        super(success, errorMessage);
        this.verb = verb;
    }

    public VerbDetailsDTO getVerb() {
        return verb;
    }

    @Override
    public String toString() {
        return "GetVerbDetailsResponseDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", verb=" + verb +
                '}';
    }
}
                                                                                                                                          