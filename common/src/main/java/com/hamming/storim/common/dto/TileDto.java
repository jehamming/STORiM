package com.hamming.storim.common.dto;

public class TileDto extends DTO {

    private byte[] imageData;

    public TileDto(Long id, byte[] imageData){
        setId(id);
        this.imageData = imageData;
    }

    public byte[] getImageData() {
        return imageData;
    }
}
