package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class UserDisconnectedDTO extends ResponseDTO {

    private Long userID;
    private String userName;

    public UserDisconnectedDTO(Long userID, String userName) {
        this.userID = userID;
        this.userName = userName;
    }

    public Long getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "UserDisconnectedDTO{" +
                "userID=" + userID +
                ", userName='" + userName + '\'' +
                '}';
    }
}
