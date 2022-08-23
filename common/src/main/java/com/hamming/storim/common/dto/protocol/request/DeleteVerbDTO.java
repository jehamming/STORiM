package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class DeleteVerbDTO extends ProtocolDTO {

    private Long verbID;

    public DeleteVerbDTO(Long verbID) {
        this.verbID = verbID;
    }


    public Long getVerbID() {
        return verbID;
    }

    @Override
    public String toString() {
        return "DeleteVerbDTO{" +
                "verbID=" + verbID +
                '}';
    }
}
