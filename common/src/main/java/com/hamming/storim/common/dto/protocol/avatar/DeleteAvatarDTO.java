package com.hamming.storim.common.dto.protocol.avatar;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class DeleteAvatarDTO implements ProtocolDTO {

    private Long avatarID;

    public DeleteAvatarDTO(Long avatarID) {
        this.avatarID = avatarID;
    }

    public Long getAvatarID() {
        return avatarID;
    }

    @Override
    public String toString() {
        return "DeleteAvatarDTO{" +
                "avatarID=" + avatarID +
                '}';
    }
}
