package com.hamming.storim.common.dto;

public class AvatarDto extends DTO {

    private byte[] imageData;

    public AvatarDto(Long id, String name, byte[] imageData){
        setId(id);
        setName(name);
        this.imageData = imageData;
    }

    public byte[] getImageData() {
        return imageData;
    }

    @Override
    public String toString() {
        return "AvatarDto{ id = "+ getId()+ "}";
    }
}
