package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class PlaceThingInRoomDTO extends ProtocolDTO {

    private Long thingId;
    private Long roomId;

    public PlaceThingInRoomDTO(Long thingId, Long roomId) {
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
