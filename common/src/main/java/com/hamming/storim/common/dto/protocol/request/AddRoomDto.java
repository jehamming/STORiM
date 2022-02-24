package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

import java.util.Arrays;

public class AddRoomDto implements ProtocolDTO {

    private String name;
    private int width, length;
    private int rows, cols;
    private Long tileId;
    private byte[] imageData;

    public AddRoomDto(String name, int width, int length, int rows, int cols, Long tileId, byte[] imageData){
        this.name = name;
        this.width = width;
        this.length = length;
        this.rows = rows;
        this.cols = cols;
        this.tileId = tileId;
        this.imageData = imageData;
    }


    public String getName() {
        return name;

    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Long getTileId() {
        return tileId;
    }

    public byte[] getImageData() {
        return imageData;
    }

    @Override
    public String toString() {
        return "AddRoomDto{" +
                ", name='" + name + '\'' +
                ", width=" + width +
                ", length=" + length +
                ", rows=" + rows +
                ", cols=" + cols +
                ", tileId=" + tileId +
                '}';
    }
}
