package com.hamming.storim.server.common.dto.protocol.dataserver.avatar;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetLocationResponseDto extends ResponseDTO {

    private Long objectId;
    private LocationDto location;

    public GetLocationResponseDto(Long objectId, LocationDto location) {
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
        return "GetLocationResponseDto{" +
                "objectId=" + objectId +
                ", location=" + location +
                '}';
    }
}
