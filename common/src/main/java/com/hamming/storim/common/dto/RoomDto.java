package com.hamming.storim.common.dto;

public class RoomDto extends DTO {

    private Long tileID;
    private int width, length;
    private int rows, cols;

    public RoomDto(Long id, String name, int width, int length, int rows, int cols, Long tileId){
        setId(id);
        setName(name);
        this.width = width;
        this.length = length;
        this.rows = rows;
        this.cols = cols;
        this.tileID = tileId;
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

    public Long getTileID() {
        return tileID;
    }

    @Override
    public String toString() {
        return "RoomDto{" +
                "id=" + getId() +
                ", name=" + getName() +
                "  tileID=" + tileID +
                ", width=" + width +
                ", length=" + length +
                ", rows=" + rows +
                ", cols=" + cols +
                '}';
    }
}
