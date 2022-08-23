package com.hamming.storim.server.common.dto.protocol.dataserver.tile;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetTilesForUserRequestDTO extends ProtocolDTO {
    private Long userId;
    public GetTilesForUserRequestDTO(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "GetTilesForUserRequestDTO{" +
                "userId=" + userId +
                '}';
    }
}
