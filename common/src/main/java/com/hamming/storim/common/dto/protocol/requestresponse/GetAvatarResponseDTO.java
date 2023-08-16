package com.hamming.storim.common.dto.protocol.requestresponse;


import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetAvatarResponseDTO extends ResponseDTO {

    private AvatarDto avatar;

    public GetAvatarResponseDTO( boolean success,  AvatarDto avatar, String errorMessage) {
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
