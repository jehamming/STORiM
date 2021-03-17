package com.hamming.storim.model.dto.protocol.thing;

import com.hamming.storim.model.dto.protocol.ProtocolDTO;

public class PlaceThingInRoomRequestDTO implements ProtocolDTO {

    private Long thingId;
    private Long roomId;

    public PlaceThingInRoomRequestDTO(Long thingId, Long roomId) {
        this.thingId = thingId;
        this.roomId = roomId;
    }

    public Long getThingId() {
        return thingId;
    }

    public Long getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return "PlaceThingInRoomDTO{" +
                "thingId=" + thingId +
                ", roomId=" + roomId +
                '}';
    }
}
