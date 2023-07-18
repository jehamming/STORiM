package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class ConnectResultDTO extends ResponseDTO {

    private boolean connectSucceeded = false;
    private String errorMessage;

    public ConnectResultDTO(boolean success, String errorMessage) {
        this.connectSucceeded = success;
        this.errorMessage = errorMessage;
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isConnectSucceeded() {
        return connectSucceeded;
    }

    @Override
    public String toString() {
        return "ConnectResultDTO{" +
                "connectSucceeded=" + connectSucceeded +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
