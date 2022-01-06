package com.hamming.storim.common.dto.protocol.user;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ProtocolSyncRequestDTO;

public class GetUserDTO extends ProtocolSyncRequestDTO {

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
