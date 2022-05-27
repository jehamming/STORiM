package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class VerbDeletedDTO implements  ResponseDTO {

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