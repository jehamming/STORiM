package com.hamming.storim.model.dto.protocol.avatar;

import com.hamming.storim.model.dto.AvatarDto;
import com.hamming.storim.model.dto.protocol.ProtocolDTO;

public class AvatarAddedDTO implements ProtocolDTO {


    private AvatarDto avatar;

    public AvatarAddedDTO(AvatarDto avatar) {
        this.avatar = avatar;
    }

    public AvatarDto getAvatar() {
        return avatar;
    }

    @Override
    public String toString() {
        return "AvatarAddedDTO{" +
                "avatar=" + avatar +
                '}';
    }
}
