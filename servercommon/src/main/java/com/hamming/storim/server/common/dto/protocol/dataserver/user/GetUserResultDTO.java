package com.hamming.storim.server.common.dto.protocol.dataserver.user;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetUserResultDTO extends ResponseDTO {

    private boolean success = false;
    private String errorMessage;
    private UserDto user;

    public GetUserResultDTO(boolean success, String errorMessage, UserDto user) {
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
        return "GetUserResultDTO{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", user=" + user +
                '}';
    }
}
                                                                                                                                          