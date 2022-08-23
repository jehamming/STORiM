package com.hamming.storim.server.common.dto.protocol.dataserver.verb;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class DeleteVerbRequestDto extends ProtocolDTO {

    private Long verbId;



    public DeleteVerbRequestDto(Long verbId){
        this.verbId = verbId;
    }

    public Long getVerbId() {
        return verbId;
    }

    @Override
    public String toString() {
        return "DeleteVerbRequestDto{" +
                "verbId=" + verbId +
                '}';
    }
}
