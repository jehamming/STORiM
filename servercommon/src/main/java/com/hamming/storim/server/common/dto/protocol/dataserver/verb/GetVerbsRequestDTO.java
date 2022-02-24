package com.hamming.storim.server.common.dto.protocol.dataserver.verb;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetVerbsRequestDTO implements ProtocolDTO {

    private Long userId;

    public GetVerbsRequestDTO(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "GetVerbsDTO{" +
                "userId=" + userId +
                '}';
    }
}
