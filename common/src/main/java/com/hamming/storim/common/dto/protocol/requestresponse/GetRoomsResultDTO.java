package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.util.HashMap;

public class GetRoomsResultDTO implements  ResponseDTO {

    private HashMap<Long, String> rooms;

    public GetRoomsResultDTO(HashMap<Long, String> rooms) {
        this.rooms = rooms;
    }

    public HashMap<Long, String> getRooms() {
        return rooms;
    }

    @Override
    public String toString() {
        return "GetRoomsResultDTO{" +
                "rooms=" + rooms +
                '}';
    }
}
