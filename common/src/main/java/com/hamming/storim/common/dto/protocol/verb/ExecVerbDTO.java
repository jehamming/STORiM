package com.hamming.storim.common.dto.protocol.verb;

import com.hamming.storim.common.dto.protocol.ProtocolASyncRequestDTO;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class ExecVerbDTO extends ProtocolASyncRequestDTO {

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
