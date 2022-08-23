package com.hamming.storim.server.common.dto.protocol.dataserver.verb;

import com.hamming.storim.common.dto.VerbDetailsDTO;
import com.hamming.storim.common.dto.VerbDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetVerbDetailsResponseDTO extends ResponseDTO {

    private boolean success = false;
    private String errorMessage;
    private VerbDetailsDTO verb;

    public GetVerbDetailsResponseDTO(boolean success, String errorMessage, VerbDetailsDTO verb) {
        this.success =success;
        this. errorMessage = errorMessage;
        this.verb = verb;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public VerbDetailsDTO getVerb() {
        return verb;
    }

    @Override
    public String toString() {
        return "GetVerbDetailsResponseDTO{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", verb=" + verb +
                '}';
    }
}
                                                                                                                                          