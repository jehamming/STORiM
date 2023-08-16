package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.ExitDto;
import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetExitResponseDTO extends ResponseDTO {

    private ExitDto exit;

    public GetExitResponseDTO(boolean success, String errorMessage, ExitDto exit) {
        super(success, errorMessage);
        this.exit = exit;
    }

    public ExitDto getExit() {
        return exit;
    }


    @Override
    public String toString() {
        return "GetExitResultDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", exit=" + exit +
                '}';
    }
}
