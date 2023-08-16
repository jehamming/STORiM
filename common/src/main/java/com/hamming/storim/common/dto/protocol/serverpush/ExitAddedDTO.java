package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.ExitDto;
import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class ExitAddedDTO extends ResponseDTO {


    private ExitDto exitDto;
    public ExitAddedDTO(ExitDto exitDto) {
        super(true, null);
        this.exitDto = exitDto;
    }

    public ExitDto getExitDto() {
        return exitDto;
    }

    @Override
    public String toString() {
        return "ExitAddedDTO{" +
                "exitDto=" + exitDto +
                '}';
    }
}
