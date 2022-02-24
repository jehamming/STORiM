package com.hamming.storim.client.listitem;

import com.hamming.storim.common.dto.RoomDto;

public class RoomDetailsListItem {

    private RoomDto roomDto;
    public RoomDetailsListItem(RoomDto roomDto) {
        this.roomDto = roomDto;
    }

    public RoomDto getRoomDto() {
        return roomDto;
    }

    @Override
    public String toString() {
        return roomDto.getName();
    }
}
