package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;

import java.util.Arrays;

public class UpdateRoomDto extends ProtocolDTO {

    private Long roomId;
    private Long tileSetId;
    private String name;
    private int rows= -1;
    private int cols= -1;

    private int[][] tileMap;
    public UpdateRoomDto(Long id, String name, int rows, int cols, Long tileSetId, int[][] tileMap){
        this.name = name;
        this.tileSetId = tileSetId;
        this.rows = rows;
        this.cols = cols;
        this.roomId = id;
        this.tileMap = tileMap;
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

    public int[][] getTileMap() {
        return tileMap;
    }

    public Long getTileSetId() {
        return tileSetId;
    }

    @Override
    public String toString() {
        return "UpdateRoomDto{" +
                "roomId=" + roomId +
                ", tileSetId=" + tileSetId +
                ", name='" + name + '\'' +
                ", rows=" + rows +
                ", cols=" + cols +
                ", tileMap=" + Arrays.toString(tileMap) +
                '}';
    }
}
