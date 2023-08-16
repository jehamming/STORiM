package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class ExitDeletedDTO extends ResponseDTO {

    private Long exitID;

    public ExitDeletedDTO(Long exitID) {
        super(true, null);
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
