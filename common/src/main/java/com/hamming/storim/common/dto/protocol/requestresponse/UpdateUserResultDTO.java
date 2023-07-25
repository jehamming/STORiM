package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class UpdateUserResultDTO extends ResponseDTO {

    private boolean success = false;
    private String errorMessage;
    private UserDto user;

    public UpdateUserResultDTO(boolean success, String errorMessage, UserDto user) {
        this.success =success;
        this.errorMessage = errorMessage;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public UserDto getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "UpdateUserResultDTO{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", user=" + user +
                '}';
    }
}
                                                                                                                                          