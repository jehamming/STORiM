package com.hamming.storim.common.dto;

public class TileSetDto extends DTO {

    private byte[] imageData;
    private int tileWidth;
    private int tileHeight;

    public TileSetDto(Long id, String name,  byte[] imageData, int tileWidth, int tileHeight){
        setId(id);
        setName(name);
        this.imageData = imageData;
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public byte[] getImageData() {
        return imageData;
    }

    @Override
    public String toString() {
        return "TileSetDto{" +
                "id=" + getId() +
                ", tileWidth=" + tileWidth +
                ", tileHeight=" + tileHeight +
                '}';
    }
}
