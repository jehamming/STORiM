package com.hamming.storim.common.dto.protocol.avatar;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;

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
