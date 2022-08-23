package com.hamming.storim.server.common.dto.protocol.dataserver.avatar;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class UpdateAvatarResponseDTO extends ResponseDTO {

    private boolean success;
    private String errorMessage;
    private AvatarDto avatar;

    public UpdateAvatarResponseDTO(boolean success, String errorMessage, AvatarDto avatar) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.avatar = avatar;
    }

    public boolean isSuccess() {
        return success;
    }

    public AvatarDto getAvatar() {
        return avatar;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "UpdateAvatarResponseDTO{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", avatar=" + avatar +
                '}';
    }
}
