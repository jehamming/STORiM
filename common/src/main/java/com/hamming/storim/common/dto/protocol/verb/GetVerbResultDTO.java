package com.hamming.storim.common.dto.protocol.verb;

import com.hamming.storim.common.dto.VerbDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ProtocolResponseDTO;

public class GetVerbResultDTO extends ProtocolResponseDTO {

    private boolean success;
    private String errorMessage;
    private VerbDto verb;

    public GetVerbResultDTO(boolean success, String errorMessage, VerbDto verb) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.verb = verb;

    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public VerbDto getVerb() {
        return verb;
    }

    @Override
    public String toString() {
        return "GetVerbResultDTO{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", verb=" + verb +
                '}';
    }
}
