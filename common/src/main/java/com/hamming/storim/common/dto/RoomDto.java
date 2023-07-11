package com.hamming.storim.common.dto;

import java.util.List;

public class RoomDto extends DTO {

    private String roomURI;
    private Long tileID;
    private int width, length;
    private int rows, cols;
    private List<Long> exits;

    public RoomDto(Long id, String roomURI, String name, int width, int length, int rows, int cols, Long tileId, List<Long> exits){
        setId(id);
        setName(name);
        this.width = width;
        this.length = length;
        this.rows = rows;
        this.cols = cols;
        this.tileID = tileId;
        this.exits = exits;
        this.roomURI = roomURI;
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

    public List<Long> getExits() {
        return exits;
    }

    public String getRoomURI() {
        return roomURI;
    }

    @Override
    public String toString() {
        return "RoomDto{" +
                "roomURI='" + roomURI + '\'' +
                ", tileID=" + tileID +
                ", width=" + width +
                ", length=" + length +
                ", rows=" + rows +
                ", cols=" + cols +
                ", exits=" + exits +
                '}';
    }
}
