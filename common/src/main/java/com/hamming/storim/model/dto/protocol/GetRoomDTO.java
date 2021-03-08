package com.hamming.storim.model.dto.protocol;

import com.hamming.storim.model.dto.DTO;

public class GetRoomDTO implements DTO {

    private Long roomID;

    public GetRoomDTO(Long roomID) {
        this.roomID = roomID;
    }

    public Long getRoomID() {
        return roomID;
    }

    @Override
    public String toString() {
        return "GetRoomDTO{" +
                "roomID=" + roomID +
                '}';
    }
}
