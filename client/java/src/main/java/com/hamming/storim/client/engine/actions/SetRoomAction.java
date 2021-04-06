package com.hamming.storim.client.engine.actions;

import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.client.view.GameView;


public class SetRoomAction implements Action {

    private GameView viewer;
    private RoomDto room;


    public SetRoomAction(GameView viewer, RoomDto room) {
        this.viewer = viewer;
        this.room = room;
    }

    @Override
    public void execute() {
        viewer.setRoom(room);
    }

    @Override
    public String toString() {
        return "SetRoomAction{" +
                "room=" + room +
                '}';
    }
}
