package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetThingsForUserDTO extends ProtocolDTO {
    private Long userId;
    public GetThingsForUserDTO(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "GetThingsForUserDTO{" +
                "userId=" + userId +
                '}';
    }
}
