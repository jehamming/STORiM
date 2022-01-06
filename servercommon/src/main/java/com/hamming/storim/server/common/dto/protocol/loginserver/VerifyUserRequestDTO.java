package com.hamming.storim.server.common.dto.protocol.loginserver;

import com.hamming.storim.common.dto.protocol.ProtocolSyncRequestDTO;

public class VerifyUserRequestDTO extends ProtocolSyncRequestDTO {

    private Long userId;
    private String token;

    public VerifyUserRequestDTO(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "VerifyUserRequestDTO{" +
                "userId=" + userId +
                ", token='" + token + '\'' +
                '}';
    }
}
