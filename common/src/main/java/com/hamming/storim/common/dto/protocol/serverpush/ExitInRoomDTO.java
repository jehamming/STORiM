package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.ExitDto;
import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class ExitInRoomDTO extends ResponseDTO {


    private ExitDto exitDto;

    public ExitInRoomDTO(ExitDto exitDto) {
        this.exitDto = exitDto;
    }

    public ExitDto getExitDto() {
        return exitDto;
    }

    @Override
    public String toString() {
        return "ExitInRoomDTO{" +
                "exitDto=" + exitDto +
                '}';
    }
}
