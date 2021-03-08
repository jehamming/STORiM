package com.hamming.storim.model.dto.protocol;

import com.hamming.storim.model.dto.DTO;

public class GetVerbDTO implements DTO {

    private Long commandID;

    public GetVerbDTO(Long commandID) {
        this.commandID = commandID;
    }

    public Long getCommandID() {
        return commandID;
    }

    @Override
    public String toString() {
        return "GetCommandDTO{" +
                "commandID=" + commandID +
                '}';
    }
}
