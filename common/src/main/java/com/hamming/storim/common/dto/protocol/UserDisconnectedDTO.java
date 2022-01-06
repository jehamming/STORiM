package com.hamming.storim.common.dto.protocol;

public class UserDisconnectedDTO extends ProtocolResponseDTO {

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
