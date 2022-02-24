package com.hamming.storim.common.dto.protocol.requestresponse;


import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetAvatarResponseDTO implements  ResponseDTO {

    private AvatarDto avatar;

    public GetAvatarResponseDTO( AvatarDto avatar) {
        this.avatar = avatar;
    }


    public AvatarDto getAvatar() {
        return avatar;
    }


    @Override
    public String toString() {
        return "GetAvatarResponseDTO{" +
                "avatar=" + avatar +
                '}';
    }
}
