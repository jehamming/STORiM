package com.hamming.storim.model.dto.protocol;

import com.hamming.storim.model.dto.DTO;

public class ExecVerbDTO implements DTO {

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
