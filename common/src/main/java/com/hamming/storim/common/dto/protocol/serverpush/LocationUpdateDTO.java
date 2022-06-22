package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class LocationUpdateDTO implements  ResponseDTO {

    private Long objectId;
    private LocationDto location;

    private Long sequenceNumber;

    public enum Type {USER,THING,EXIT};

    private Type type;

    public LocationUpdateDTO(Type type, Long objectId, LocationDto location) {
        this.objectId = objectId;
        this.location = location;
        this.type = type;
    }

    public Long getObjectId() {
        return objectId;
    }

    public LocationDto getLocation() {
        return location;
    }

    public Long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "LocationUpdateDTO{" +
                "objectId=" + objectId +
                ", location=" + location +
                ", sequenceNumber=" + sequenceNumber +
                ", type=" + type +
                '}';
    }
}
