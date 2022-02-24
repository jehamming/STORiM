package com.hamming.storim.server.common.dto.protocol.dataserver.avatar;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

import java.util.Arrays;

public class UpdateAvatarRequestDto implements ProtocolDTO {

    private Long avatarId;
    private String name;
    private byte[] imageData;

    public UpdateAvatarRequestDto(Long avatarId, String name, byte[] imageData){
        this.name = name;
        this.imageData = imageData;
        this.avatarId = avatarId;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    public String getName() {
        return name;
    }

    public byte[] getImageData() {
        return imageData;
    }

    @Override
    public String toString() {
        return "UpdateAvatarRequestDto{" +
                "avatarId=" + avatarId +
                ", name='" + name + '\'' +
                '}';
    }
}
