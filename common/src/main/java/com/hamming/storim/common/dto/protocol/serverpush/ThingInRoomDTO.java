package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class ThingInRoomDTO implements  ResponseDTO {


    private ThingDto thing;

    private LocationDto location;

    public ThingInRoomDTO(ThingDto thing, LocationDto location) {
        this.thing = thing;
        this.location = location;
    }

    public ThingDto getThing() {
        return thing;
    }

    public LocationDto getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "ThingInRoomDTO{" +
                "thing=" + thing +
                ", location=" + location +
                '}';
    }
}
