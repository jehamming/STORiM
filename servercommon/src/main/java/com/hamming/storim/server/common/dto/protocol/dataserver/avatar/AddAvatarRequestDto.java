package com.hamming.storim.server.common.dto.protocol.dataserver.avatar;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class AddAvatarRequestDto extends ProtocolDTO {

    private Long userId;
    private String name;
    private byte[] imageData;

    public AddAvatarRequestDto(Long userId, String name, byte[] imageData){
        this.name = name;
        this.imageData = imageData;
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public byte[] getImageData() {
        return imageData;
    }

    @Override
    public String toString() {
        return "AddAvatarRequestDto{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                '}';
    }
}
