package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class AddUserResultDTO extends ResponseDTO {

    private UserDto userDto;

    public AddUserResultDTO(boolean success, String errorMessage, UserDto userDto) {
        super(success, errorMessage);
        this.userDto = userDto;
    }


    public UserDto getUserDto() {
        return userDto;
    }

    @Override
    public String toString() {
        return "AddUserResultDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", userDto=" + userDto +
                '}';
    }
}
