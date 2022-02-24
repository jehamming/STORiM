package com.hamming.storim.client.listitem;

import com.hamming.storim.common.dto.RoomDto;

public class RoomListItem {

    private Long id;
    private String roomName;

    public RoomListItem(Long id, String name) {
        this.id = id;
        this.roomName = name;
    }

    public Long getId() {
        return id;
    }

    public String getRoomName() {
        return roomName;
    }

    @Override
    public String toString() {
        return "(" + id + ") " + roomName;
    }
}
