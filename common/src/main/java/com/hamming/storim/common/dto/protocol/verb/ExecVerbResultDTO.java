package com.hamming.storim.common.dto.protocol.verb;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ProtocolResponseDTO;

public class ExecVerbResultDTO extends ProtocolResponseDTO {

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
