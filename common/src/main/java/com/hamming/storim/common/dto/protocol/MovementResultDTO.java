package com.hamming.storim.common.dto.protocol;

import com.hamming.storim.common.dto.LocationDto;

public class MovementResultDTO implements ProtocolDTO {
    private long sequence;
    private LocationDto location;


    public MovementResultDTO(long sequence, LocationDto location) {
        this.sequence = sequence;
        this.location = location;
    }

    public long getSequence() {
        return sequence;
    }

    public LocationDto getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "MovementResultDTO{" +
                "sequence=" + sequence +
                ", location=" + location +
                '}';
    }
}
