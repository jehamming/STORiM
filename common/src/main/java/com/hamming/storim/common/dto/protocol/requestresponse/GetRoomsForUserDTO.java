package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetRoomsForUserDTO extends ProtocolDTO {
    private Long userId;
    public GetRoomsForUserDTO(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "GetRoomsForUserDTO{" +
                "userId=" + userId +
                '}';
    }
}
