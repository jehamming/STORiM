package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class AddUserResultDTO extends ResponseDTO {

    private boolean success = false;
    private String errorMessage;
    private UserDto userDto;

    public AddUserResultDTO(boolean success, String errorMessage, UserDto userDto) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.userDto = userDto;
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "AddUserResultDTO{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", userDto=" + userDto +
                '}';
    }
}
