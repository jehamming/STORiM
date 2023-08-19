package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

import java.util.Arrays;
import java.util.List;

public class   UpdateRoomDto extends ProtocolDTO {

    private Long roomId;
    private String name;
    private int rows= -1;
    private int cols= -1;

    private Long backTileSetId;
    private Long frontTileSetId;
    private int[][] backTileMap;
    private int[][] frontTileMap;

    public UpdateRoomDto(Long id, String name, int rows, int cols, Long backTileSetId, int[][] backTileMap, Long frontTileSetId, int[][] frontTileMap){
        this.name = name;
        this.rows = rows;
        this.cols = cols;
        this.roomId = id;
        this.backTileMap = backTileMap;
        this.backTileSetId = backTileSetId;
        this.frontTileMap = frontTileMap;
        this.frontTileSetId = frontTileSetId;
    }


    public String getName() {
        return name;

    }

    public Long getRoomId() {
        return roomId;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
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
        return "UpdateRoomDto{" +
                "roomId=" + roomId +
                ", name='" + name + '\'' +
                ", rows=" + rows +
                ", cols=" + cols +
                ", backTileSetId=" + backTileSetId +
                ", frontTileSetId=" + frontTileSetId +
                ", backTileMap=" + Arrays.toString(backTileMap) +
                ", frontTileMap=" + Arrays.toString(frontTileMap) +
                '}';
    }
}
