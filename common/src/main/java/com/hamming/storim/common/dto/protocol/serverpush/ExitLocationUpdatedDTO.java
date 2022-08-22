package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.ExitDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class ExitLocationUpdatedDTO implements  ResponseDTO {


    private Long exitId;
    private int x;
    private int y;

    public ExitLocationUpdatedDTO(Long exitId, int x, int y) {
        this.exitId = exitId;
        this.x = x;
        this.y =y;
    }

    public Long getExitId() {
        return exitId;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    @Override
    public String toString() {
        return "ExitLocationUpdatedDTO{" +
                "exitId=" + exitId +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
