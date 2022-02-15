package com.hamming.storim.server.common.dto.protocol.dataserver.user;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetUserRequestDTO implements ProtocolDTO {

    private Long userId;

    public GetUserRequestDTO(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "GetUserRequestDTO{" +
                "userId=" + userId +
                '}';
    }
}
