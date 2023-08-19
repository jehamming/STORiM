package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

import java.util.List;

public class AddTileSetDto extends ProtocolDTO {

    private String name;
    private int tileWidth;
    private int tileHeight;
    private byte[] imageData;

    public AddTileSetDto(String name, int tileWidth, int tileHeight, byte[] imageData){
        this.name = name;
        this.imageData = imageData;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public String getName() {
        return name;

    }

    public byte[] getImageData() {
        return imageData;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    @Override
    public String toString() {
        return "AddTileSetDto{" +
                "name='" + name + '\'' +
                ", tileWidth=" + tileWidth +
                ", tileHeight=" + tileHeight +
                '}';
    }
}
