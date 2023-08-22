package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class LoginWithTokenDTO extends ProtocolDTO {


    private Long userId;
    private String token;
    private Long roomId;

    public LoginWithTokenDTO(Long userId, String token, Long roomId){
        this.userId = userId;
        this.token = token;
        this.roomId = roomId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public Long getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return "LoginWithTokenDTO{" +
                "userId=" + userId +
                ", token='" + token + '\'' +
                ", roomId=" + roomId +
                '}';
    }
}
