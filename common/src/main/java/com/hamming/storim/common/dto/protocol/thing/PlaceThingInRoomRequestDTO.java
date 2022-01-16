package com.hamming.storim.common.dto.protocol.thing;

import com.hamming.storim.common.dto.protocol.ProtocolASyncRequestDTO;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class PlaceThingInRoomRequestDTO extends ProtocolASyncRequestDTO {

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