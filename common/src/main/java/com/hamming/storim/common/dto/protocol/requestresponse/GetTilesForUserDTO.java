package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetTilesForUserDTO implements ProtocolDTO {
    private Long userId;
    public GetTilesForUserDTO(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "GetTilesForUserDTO{" +
                "userId=" + userId +
                '}';
    }
}
