package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class ConnectResultDTO extends ResponseDTO {

    public ConnectResultDTO(boolean success, String errorMessage) {
        super(success, errorMessage);
    }


    @Override
    public String toString() {
        return "ConnectResultDTO{" +
                "connectSucceeded=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                '}';
    }
}
