package com.hamming.storim.model.dto.protocol.avatar;

import com.hamming.storim.model.dto.AvatarDto;
import com.hamming.storim.model.dto.protocol.ProtocolDTO;

public class AvatarUpdatedDTO implements ProtocolDTO {


    private AvatarDto avatar;

    public AvatarUpdatedDTO(AvatarDto avatar) {
        this.avatar = avatar;
    }

    public AvatarDto getAvatar() {
        return avatar;
    }

    @Override
    public String toString() {
        return "AvatarUpdatedDTO{" +
                "avatar=" + avatar +
                '}';
    }
}
