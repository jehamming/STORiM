package com.hamming.storim.model.dto.protocol;

import com.hamming.storim.model.dto.VerbDto;
import com.hamming.storim.model.dto.DTO;

public class GetVerbResultDTO implements DTO {

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
