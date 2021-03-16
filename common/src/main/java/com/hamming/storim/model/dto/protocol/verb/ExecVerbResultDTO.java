package com.hamming.storim.model.dto.protocol.verb;

import com.hamming.storim.model.dto.DTO;
import com.hamming.storim.model.dto.protocol.ProtocolDTO;

public class ExecVerbResultDTO implements ProtocolDTO {

    private Long verbID;
    private boolean success;
    private String errorMessage;
    private String output;

    public ExecVerbResultDTO(Long verbId, boolean success, String errorMessage, String output) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.output = output;
        this.verbID = verbId;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getOutput() {
        return output;
    }

    @Override
    public String toString() {
        return "ExecVerbResultDTO{" +
                "verbID=" + verbID +
                ", success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", output='" + output + '\'' +
                '}';
    }
}
