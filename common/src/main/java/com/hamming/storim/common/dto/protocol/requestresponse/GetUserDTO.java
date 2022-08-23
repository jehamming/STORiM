package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetUserDTO extends ProtocolDTO {

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
