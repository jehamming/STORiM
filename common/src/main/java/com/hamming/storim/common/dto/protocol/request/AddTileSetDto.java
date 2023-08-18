package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

import java.util.List;

public class AddTileSetDto extends ProtocolDTO {

    private String name;
    private int tileWidth;
    private int tileHeight;
    private byte[] imageData;
    private List<Long> editors;

    public AddTileSetDto(String name, int tileWidth, int tileHeight, byte[] imageData, List<Long> editors){
        this.name = name;
        this.imageData = imageData;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.editors = editors;
    }

    public String getName() {
        return name;

    }

    public List<Long> getEditors() {
        return editors;
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
                ", editors=" + editors +
                '}';
    }
}
