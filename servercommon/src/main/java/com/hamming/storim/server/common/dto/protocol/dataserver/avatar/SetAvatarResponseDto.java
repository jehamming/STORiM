package com.hamming.storim.server.common.dto.protocol.dataserver.avatar;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class SetAvatarResponseDto extends ResponseDTO {

    private boolean success;
    private String errorMessage;
    private AvatarDto avatar;

    public SetAvatarResponseDto(boolean success, String errorMessage, AvatarDto avatar) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.avatar = avatar;
    }

    public AvatarDto getAvatar() {
        return avatar;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "SetAvatarResponseDto{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", avatar=" + avatar +
                '}';
    }
}
