package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetExitDTO extends ProtocolDTO {

    private Long exitID;
    private Long roomID;


    public GetExitDTO(Long roomID, Long exitID) {
        this.exitID = exitID;
        this.roomID = roomID;
    }

    public Long getExitID() {
        return exitID;
    }

    public Long getRoomID() {
        return roomID;
    }

    @Override
    public String toString() {
        return "GetExitDTO{" +
                "exitID=" + exitID +
                ", roomID=" + roomID +
                '}';
    }
}
