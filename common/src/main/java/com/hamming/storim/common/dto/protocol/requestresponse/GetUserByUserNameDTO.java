package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetUserByUserNameDTO extends ProtocolDTO {

    private String userName;

    public GetUserByUserNameDTO(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "GetUserByUserNameDTO{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
