package com.hamming.storim.common.dto.protocol.avatar;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ProtocolResponseDTO;

public class AvatarAddedDTO extends ProtocolResponseDTO {


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
