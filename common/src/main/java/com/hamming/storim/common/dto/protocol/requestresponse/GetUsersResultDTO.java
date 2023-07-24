package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.util.HashMap;

public class GetUsersResultDTO extends ResponseDTO {

    private HashMap<Long, String> users;
    private String errorMessage;

    public GetUsersResultDTO(HashMap<Long, String> users, String errorMessage) {
        this.users = users;
        this.errorMessage = errorMessage;
    }

    public HashMap<Long, String> getUsers() {
        return users;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "GetUsersResultDTO{" +
                "users=" + users +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
