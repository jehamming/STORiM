package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.util.HashMap;

public class SearchUsersResultDTO extends ResponseDTO {

    private HashMap<Long, String> users;

    public SearchUsersResultDTO(boolean success, HashMap<Long, String> users, String errorMessage) {
        super(success, errorMessage);
        this.users = users;
    }

    public HashMap<Long, String> getUsers() {
        return users;
    }

    @Override
    public String toString() {
        return "SearchUsersResultDTO{" +
                "users=" + users +
                '}';
    }
}
