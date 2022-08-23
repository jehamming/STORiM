package com.hamming.storim.server.common.dto.protocol.dataserver.avatar;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class SetLocationDto extends ProtocolDTO {

    private Long objectId;
    private LocationDto location;


    public SetLocationDto(Long objectId, LocationDto location) {
        this.location = location;
        this.objectId = objectId;
    }

    public LocationDto getLocation() {
        return location;
    }

    public Long getObjectId() {
        return objectId;
    }

    @Override
    public String toString() {
        return "SetLocationDto{" +
                "objectId=" + objectId +
                ", location=" + location +
                '}';
    }
}
