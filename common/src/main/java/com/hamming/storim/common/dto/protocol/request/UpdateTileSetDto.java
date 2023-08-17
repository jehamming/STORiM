package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class UpdateTileSetDto extends ProtocolDTO {

    private Long id;
    private String name;
    private int tileWidth;
    private int tileHeight;
    private byte[] imageData;


    public UpdateTileSetDto(Long id, String name, int tileWidth, int tileHeight, byte[] imageData){
        this.id = id;
        this.name = name;
        this.imageData = imageData;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
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
        return "UpdateTileSetDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tileWidth=" + tileWidth +
                ", tileHeight=" + tileHeight +
                '}';
    }
}
