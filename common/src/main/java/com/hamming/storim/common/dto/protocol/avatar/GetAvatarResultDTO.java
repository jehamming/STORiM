package com.hamming.storim.common.dto.protocol.avatar;


import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetAvatarResultDTO implements ProtocolDTO {

    private boolean success = false;
    private String errorMessage;
    private AvatarDto avatar;
    private Long userID;

    public GetAvatarResultDTO(boolean success, String errorMessage, Long userId,  AvatarDto avatar) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.avatar = avatar;
        this.userID = userId;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public AvatarDto getAvatar() {
        return avatar;
    }

    public Long getUserID() {
        return userID;
    }

    @Override
    public String toString() {
        return "GetAvatarResultDTO{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", avatar=" + avatar +
                ", userID=" + userID +
                '}';
    }
}
