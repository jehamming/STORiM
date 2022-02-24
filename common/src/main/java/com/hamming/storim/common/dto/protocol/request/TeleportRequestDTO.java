package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class TeleportRequestDTO implements ProtocolDTO {

    private Long userID;
    private Long roomID;

    public TeleportRequestDTO( Long userID, Long roomID) {
        this.userID = userID;
        this.roomID = roomID;
    }

    public Long getUserID() {
        return userID;
    }

    public Long getRoomID() {
        return roomID;
    }

    @Override
    public String toString() {
        return "TeleportProtocolDTO{" +
                "userID=" + userID +
                ", roomID=" + roomID +
                '}';
    }
}
