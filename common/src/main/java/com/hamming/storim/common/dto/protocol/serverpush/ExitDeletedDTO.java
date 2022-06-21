package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class ExitDeletedDTO implements  ResponseDTO {

    private Long exitID;

    public ExitDeletedDTO(Long exitID) {
        this.exitID = exitID;
    }

    public Long getExitID() {
        return exitID;
    }

    @Override
    public String toString() {
        return "ExitDeletedDTO{" +
                "exitID=" + exitID +
                '}';
    }
}
