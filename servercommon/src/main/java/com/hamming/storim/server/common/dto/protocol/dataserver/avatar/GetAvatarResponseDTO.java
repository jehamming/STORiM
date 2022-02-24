package com.hamming.storim.server.common.dto.protocol.dataserver.avatar;


import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetAvatarResponseDTO implements  ResponseDTO {

    private boolean success = false;
    private String errorMessage;
    private AvatarDto avatar;

    public GetAvatarResponseDTO(boolean success, String errorMessage, AvatarDto avatar) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.avatar = avatar;
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


    @Override
    public String toString() {
        return "GetAvatarResponseDTO{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", avatar=" + avatar +
                '}';
    }
}
