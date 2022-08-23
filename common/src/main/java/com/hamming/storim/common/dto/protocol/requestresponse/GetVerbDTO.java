package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetVerbDTO extends ProtocolDTO {

    private Long verbID;

    public GetVerbDTO(Long verbID) {
        this.verbID = verbID;
    }

    public Long getVerbID() {
        return verbID;
    }

    @Override
    public String toString() {
        return "GetVerbDTO{" +
                "verbID=" + verbID +
                '}';
    }
}
