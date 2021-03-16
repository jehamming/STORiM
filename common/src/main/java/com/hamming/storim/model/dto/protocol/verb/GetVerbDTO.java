package com.hamming.storim.model.dto.protocol.verb;

import com.hamming.storim.model.dto.DTO;
import com.hamming.storim.model.dto.protocol.ProtocolDTO;

public class GetVerbDTO implements ProtocolDTO {

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
