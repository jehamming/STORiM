package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class DeleteExitDTO implements ProtocolDTO {

    private Long exitID;

    public DeleteExitDTO(Long exitID) {
        this.exitID = exitID;
    }

    public Long getExitID() {
        return exitID;
    }

    @Override
    public String toString() {
        return "DeleteExitDTO{" +
                "exitID=" + exitID +
                '}';
    }
}
