package com.hamming.storim.common.dto;

import java.util.Arrays;
import java.util.List;

public class RoomDto extends DTO {

    private String roomURI;
    private Long tileSetId;
    private int rows, cols;
    private List<Long> exits;
    private int[][] tileMap;

    public RoomDto(Long id, String roomURI, String name, int rows, int cols, Long tileSetId, int[][] tileMap, List<Long> exits){
        setId(id);
        setName(name);
        this.rows = rows;
        this.cols = cols;
        this.exits = exits;
        this.roomURI = roomURI;
        this.tileMap = tileMap;
        this.tileSetId = tileSetId;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Long getTileSetId() {
        return tileSetId;
    }

    public int[][] getTileMap() {
        return tileMap;
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
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", roomURI='" + roomURI + '\'' +
                ", tileSetId=" + tileSetId +
                ", rows=" + rows +
                ", cols=" + cols +
                ", exits=" + exits +
                ", tileMap=" + Arrays.toString(tileMap) +
                '}';
    }
}
