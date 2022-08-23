package com.hamming.storim.server.common.dto.protocol.dataserver.avatar;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class DeleteAvatarRequestDTO extends ProtocolDTO {

    private Long avatarID;

    public DeleteAvatarRequestDTO(Long avatarID) {
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
