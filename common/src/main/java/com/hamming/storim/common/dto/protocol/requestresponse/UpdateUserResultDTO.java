package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class UpdateUserResultDTO extends ResponseDTO {

    private UserDto user;

    public UpdateUserResultDTO(boolean success, String errorMessage, UserDto user) {
        super(success, errorMessage);
        this.user = user;
    }

    public UserDto getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "UpdateUserResultDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", user=" + user +
                '}';
    }
}
                                                                                                                                          