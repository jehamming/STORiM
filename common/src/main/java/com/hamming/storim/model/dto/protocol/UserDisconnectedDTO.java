package com.hamming.storim.model.dto.protocol;

import com.hamming.storim.model.dto.DTO;

public class UserDisconnectedDTO implements DTO {

    private Long userID;

    public UserDisconnectedDTO(Long userID) {
        this.userID = userID;
    }

    public Long getUserID() {
        return userID;
    }

    @Override
    public String toString() {
        return "UserDisconnectedDTO{" +
                "userID=" + userID +
                '}';
    }
}
