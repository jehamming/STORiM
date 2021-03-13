package com.hamming.storim.model.dto.protocol;

import com.hamming.storim.model.dto.DTO;

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
        return "TeleportRequestDTO{" +
                "userID=" + userID +
                ", roomID=" + roomID +
                '}';
    }
}
