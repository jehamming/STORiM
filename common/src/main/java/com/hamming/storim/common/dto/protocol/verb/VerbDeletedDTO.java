package com.hamming.storim.common.dto.protocol.verb;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ProtocolResponseDTO;

public class VerbDeletedDTO extends ProtocolResponseDTO {

    private Long verbID;

    public VerbDeletedDTO(Long verbID) {
        this.verbID = verbID;
    }


    public Long getVerbID() {
        return verbID;
    }


    @Override
    public String toString() {
        return "VerbDeletedDTO{" +
                "verbID=" + verbID +
                '}';
    }
}
