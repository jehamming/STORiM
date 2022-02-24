package com.hamming.storim.server.common.dto.protocol.dataserver.verb;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetVerbDetailsRequestDTO implements ProtocolDTO {

    private Long verbID;

    public GetVerbDetailsRequestDTO(Long verbID) {
        this.verbID = verbID;
    }

    public Long getVerbID() {
        return verbID;
    }

    @Override
    public String toString() {
        return "GetVerbDetailsRequestDTO{" +
                "verbID=" + verbID +
                '}';
    }
}
