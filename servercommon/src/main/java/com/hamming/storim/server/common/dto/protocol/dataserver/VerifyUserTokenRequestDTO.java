package com.hamming.storim.server.common.dto.protocol.dataserver;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class VerifyUserTokenRequestDTO extends ProtocolDTO {

    private String source;
    private Long userId;
    private String token;

    public VerifyUserTokenRequestDTO(String source, Long userId, String token) {
        this.userId = userId;
        this.token = token;
        this.source = source;
    }

    public Long getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "VerifyUserTokenRequestDTO{" +
                "source='" + source + '\'' +
                ", userId=" + userId +
                ", token='" + token + '\'' +
                '}';
    }
}
