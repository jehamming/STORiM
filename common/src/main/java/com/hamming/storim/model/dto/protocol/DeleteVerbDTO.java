package com.hamming.storim.model.dto.protocol;

import com.hamming.storim.model.dto.DTO;

public class DeleteVerbDTO implements DTO {

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
