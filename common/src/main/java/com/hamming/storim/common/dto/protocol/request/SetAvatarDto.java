package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class SetAvatarDto extends ProtocolDTO {

    private Long avatarId;

    public SetAvatarDto(Long avatarId) {
        this.avatarId = avatarId;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    @Override
    public String toString() {
        return "SetAvatarDto{" +
                "avatarId=" + avatarId +
                '}';
    }
}
