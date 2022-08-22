package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class UpdateExitLocationDto implements ProtocolDTO {

    private Long exitId;
    private Long roomId;
    private int x;
    private int y;


    public UpdateExitLocationDto(Long exitId, Long roomId, int x, int y){
        this.exitId = exitId;
        this.roomId = roomId;
        this.x = x;
        this.y = y;
    }

    public Long getExitId() {
        return exitId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Long getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return "UpdateExitLocationDto{" +
                "exitId=" + exitId +
                ", roomId=" + roomId +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
