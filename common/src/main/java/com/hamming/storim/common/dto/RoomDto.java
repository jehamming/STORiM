package com.hamming.storim.common.dto;

import java.util.Arrays;
import java.util.List;

public class RoomDto extends BasicObjectDTO {

    private String roomURI;
    private int rows, cols;
    private List<Long> exits;
    private Long backTileSetId;
    private Long frontTileSetId;
    private int[][] backTileMap;
    private int[][] frontTileMap;


    public RoomDto(Long id, String roomURI, String name, int rows, int cols, Long backTileSetId, int[][] backTileMap, Long frontTileSetId, int[][] frontTileMap, List<Long> exits){
        setId(id);
        setName(name);
        this.rows = rows;
        this.cols = cols;
        this.exits = exits;
        this.roomURI = roomURI;
        this.frontTileMap = frontTileMap;
        this.frontTileSetId = frontTileSetId;
        this.backTileMap = backTileMap;
        this.backTileSetId = backTileSetId;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public List<Long> getExits() {
        return exits;
    }

    public String getRoomURI() {
        return roomURI;
    }

    public Long getBackTileSetId() {
        return backTileSetId;
    }

    public Long getFrontTileSetId() {
        return frontTileSetId;
    }

    public int[][] getBackTileMap() {
        return backTileMap;
    }

    public int[][] getFrontTileMap() {
        return frontTileMap;
    }

    @Override
    public String toString() {
        return "RoomDto{" +
                "roomURI='" + roomURI + '\'' +
                ", rows=" + rows +
                ", cols=" + cols +
                ", exits=" + exits +
                ", backTileSetId=" + backTileSetId +
                ", frontTileSetId=" + frontTileSetId +
                ", backTileMap=" + Arrays.toString(backTileMap) +
                ", frontTileMap=" + Arrays.toString(frontTileMap) +
                '}';
    }
}
