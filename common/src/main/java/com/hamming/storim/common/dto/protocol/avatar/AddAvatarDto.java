package com.hamming.storim.common.dto.protocol.avatar;

import com.hamming.storim.common.dto.protocol.ProtocolASyncRequestDTO;

public class AddAvatarDto extends ProtocolASyncRequestDTO {

    private String name;

    private byte[] imageData;

    public AddAvatarDto(String name,byte[] imageData){
        this.name = name;
        this.imageData = imageData;
    }

    public String getName() {
        return name;

    }

    public byte[] getImageData() {
        return imageData;
    }

    @Override
    public String toString() {
        return "AddAvatarDto{" +
                "name='" + name + '\'' +
                '}';
    }
}
