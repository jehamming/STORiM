package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class ExecVerbDTO implements ProtocolDTO {

    private Long verbId;
    private String input;

    public ExecVerbDTO(Long verbId, String input) {
        this.verbId = verbId;
        this.input = input;
    }


    public Long getVerbId() {
        return verbId;
    }

    public String getInput() {
        return input;
    }

    @Override
    public String toString() {
        return "ExecVerbDTO{" +
                "verbId=" + verbId +
                ", input='" + input + '\'' +
                '}';
    }
}
