package com.hamming.storim.model.dto.protocol;

import com.hamming.storim.model.dto.DTO;
import com.hamming.storim.model.dto.UserDto;

public class GetUserResultDTO implements DTO {

    private boolean success = false;
    private String errorMessage;
    private UserDto user;

    public GetUserResultDTO(boolean success, String errorMessage, UserDto user) {
        this.success =success;
        this. errorMessage = errorMessage;
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
        return "GetUserResultDTO{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", user=" + user +
                '}';
    }
}
                                                                                                                                          