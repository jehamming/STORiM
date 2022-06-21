package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.ExitDto;
import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class ExitInRoomDTO implements  ResponseDTO {


    private ExitDto exitDto;
    private LocationDto locationDto;

    public ExitInRoomDTO(ExitDto exitDto, LocationDto locationDto) {
        this.exitDto = exitDto;
        this.locationDto = locationDto;
    }

    public ExitDto getExitDto() {
        return exitDto;
    }

    public LocationDto getLocationDto() {
        return locationDto;
    }

    @Override
    public String toString() {
        return "ExitInRoomDTO{" +
                "exitDto=" + exitDto +
                ", locationDto=" + locationDto +
                '}';
    }
}
