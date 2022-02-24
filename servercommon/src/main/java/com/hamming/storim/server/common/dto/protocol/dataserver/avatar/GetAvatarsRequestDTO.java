package com.hamming.storim.server.common.dto.protocol.dataserver.avatar;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetAvatarsRequestDTO implements ProtocolDTO {
    private Long userId;
    public GetAvatarsRequestDTO(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "GetAvatarsRequestDTO{" +
                "userId=" + userId +
                '}';
    }
}
