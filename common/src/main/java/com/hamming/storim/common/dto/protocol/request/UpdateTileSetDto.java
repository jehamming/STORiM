package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

import java.util.Arrays;
import java.util.List;

public class UpdateTileSetDto extends ProtocolDTO {

    private Long id;
    private String name;
    private int tileWidth;
    private int tileHeight;
    private byte[] imageData;
    private List<Long> editors;


    public UpdateTileSetDto(Long id, String name, int tileWidth, int tileHeight, byte[] imageData, List<Long> editors){
        this.id = id;
        this.name = name;
        this.imageData = imageData;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.editors = editors;
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

    public List<Long> getEditors() {
        return editors;
    }

    @Override
    public String toString() {
        return "UpdateTileSetDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tileWidth=" + tileWidth +
                ", tileHeight=" + tileHeight +
                ", editors=" + editors +
                '}';
    }
}
