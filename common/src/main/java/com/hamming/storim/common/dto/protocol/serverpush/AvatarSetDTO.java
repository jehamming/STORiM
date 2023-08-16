package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class AvatarSetDTO extends ResponseDTO {


    private AvatarDto avatar;
    private Long userId;

    public AvatarSetDTO(Long userId, AvatarDto avatar) {
        super(true, null);
        this.avatar = avatar;
        this.userId = userId;
    }

    public AvatarDto getAvatar() {
        return avatar;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "AvatarSetDTO{" +
                "userId=" + userId +
                ", avatarId=" + avatar.getId() +
                '}';
    }
}
