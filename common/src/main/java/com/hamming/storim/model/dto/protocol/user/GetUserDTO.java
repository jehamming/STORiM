package com.hamming.storim.model.dto.protocol.user;

import com.hamming.storim.model.dto.DTO;
import com.hamming.storim.model.dto.protocol.ProtocolDTO;

public class GetUserDTO implements ProtocolDTO {

    private Long userID;

    public GetUserDTO(Long userID) {
        this.userID = userID;
    }

    public Long getUserID() {
        return userID;
    }

    @Override
    public String toString() {
        return "GetUserDTO{" +
                "userID=" + userID +
                '}';
    }
}