package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class UpdateAvatarDto extends ProtocolDTO {

    private Long avatarId;
    private String name;
    private byte[] imageData;


    public UpdateAvatarDto(Long avatarId,String name,byte[] imageData){
        this.name = name;
        this.imageData = imageData;
        this.avatarId = avatarId;
    }

    public String getName() {
        return name;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    public byte[] getImageData() {
        return imageData;
    }

    @Override
    public String toString() {
        return "UpdateAvatarDto{" +
                "avatarId=" + avatarId +
                ", name='" + name + '\'' +
                '}';
    }
}
