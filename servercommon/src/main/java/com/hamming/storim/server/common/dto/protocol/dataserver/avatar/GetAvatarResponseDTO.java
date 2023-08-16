package com.hamming.storim.server.common.dto.protocol.dataserver.avatar;


import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetAvatarResponseDTO extends ResponseDTO {

    private AvatarDto avatar;

    public GetAvatarResponseDTO(boolean success, String errorMessage, AvatarDto avatar) {
        super(success, errorMessage);
        this.avatar = avatar;
    }

    public AvatarDto getAvatar() {
        return avatar;
    }


    @Override
    public String toString() {
        return "GetAvatarResponseDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", avatar=" + avatar +
                '}';
    }
}
