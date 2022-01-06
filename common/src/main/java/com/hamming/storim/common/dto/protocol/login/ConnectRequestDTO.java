package com.hamming.storim.common.dto.protocol.login;

import com.hamming.storim.common.dto.protocol.ProtocolASyncRequestDTO;

public class ConnectRequestDTO extends ProtocolASyncRequestDTO {


    private Long userId;
    private String token;

    public ConnectRequestDTO(Long userId, String token){
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
        return "ConnectRequestDTO{" +
                "userId=" + userId +
                ", token='" + token + '\'' +
                '}';
    }
}
