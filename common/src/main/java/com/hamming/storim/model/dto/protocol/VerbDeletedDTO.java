package com.hamming.storim.model.dto.protocol;

import com.hamming.storim.model.dto.DTO;

public class VerbDeletedDTO implements ProtocolDTO {

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
