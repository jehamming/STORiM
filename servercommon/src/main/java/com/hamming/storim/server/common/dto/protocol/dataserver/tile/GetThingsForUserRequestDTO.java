package com.hamming.storim.server.common.dto.protocol.dataserver.tile;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetThingsForUserRequestDTO implements ProtocolDTO {
    private Long userId;
    public GetThingsForUserRequestDTO(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "GetThingsForUserRequestDTO{" +
                "userId=" + userId +
                '}';
    }
}
