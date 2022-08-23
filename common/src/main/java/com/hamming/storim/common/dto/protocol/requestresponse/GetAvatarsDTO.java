package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetAvatarsDTO extends ProtocolDTO {
    private Long userId;
    public GetAvatarsDTO(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "GetAvatarsDTO{" +
                "userId=" + userId +
                '}';
    }
}
