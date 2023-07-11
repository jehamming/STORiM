package com.hamming.storim.server.common.dto.protocol.dataserver;

import com.hamming.storim.common.dto.DTO;

public class SessionDto extends DTO {

    private String token;
    private Long userId;

    public SessionDto(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "SessionDto{" +
                "token='" + token + '\'' +
                ", userId=" + userId +
                '}';
    }
}

