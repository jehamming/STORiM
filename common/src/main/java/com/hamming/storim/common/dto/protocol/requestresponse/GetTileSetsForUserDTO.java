package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetTileSetsForUserDTO extends ProtocolDTO {
    private Long userId;
    public GetTileSetsForUserDTO(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "GetTileSetsForUserDTO{" +
                "userId=" + userId +
                '}';
    }
}
