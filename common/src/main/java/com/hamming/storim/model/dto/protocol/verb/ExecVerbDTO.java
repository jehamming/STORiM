package com.hamming.storim.model.dto.protocol.verb;

import com.hamming.storim.model.dto.DTO;
import com.hamming.storim.model.dto.protocol.ProtocolDTO;

public class ExecVerbDTO implements ProtocolDTO {

    private Long commandID;
    private String input;

    public ExecVerbDTO(Long commandID, String input) {
        this.commandID = commandID;
        this.input = input;
    }


    public Long getCommandID() {
        return commandID;
    }

    public String getInput() {
        return input;
    }

    @Override
    public String toString() {
        return "ExecCommandDTO{" +
                "commandID=" + commandID +
                ", input='" + input + '\'' +
                '}';
    }
}
