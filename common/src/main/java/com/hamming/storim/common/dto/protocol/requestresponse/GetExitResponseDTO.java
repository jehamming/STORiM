package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.ExitDto;
import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetExitResponseDTO implements  ResponseDTO {

    private boolean success = false;
    private String errorMessage;
    private ExitDto exit;

    public GetExitResponseDTO(boolean success, String errorMessage, ExitDto exit) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.exit = exit;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ExitDto getExit() {
        return exit;
    }


    @Override
    public String toString() {
        return "GetExitResultDTO{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", exit=" + exit +
                '}';
    }
}
