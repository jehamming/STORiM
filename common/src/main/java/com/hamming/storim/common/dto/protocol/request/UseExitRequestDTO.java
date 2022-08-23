package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class UseExitRequestDTO extends ProtocolDTO {

    private Long exitId;

    public UseExitRequestDTO(Long exitId) {
        this.exitId = exitId;
    }

    public Long getExitId() {
        return exitId;
    }

    @Override
    public String toString() {
        return "UseExitRequestDTO{" +
                "exitId=" + exitId +
                '}';
    }
}
