package nl.hamming.storimapp.engine.actions;

import com.hamming.storim.model.dto.RoomDto;
import nl.hamming.storimapp.view.GameView;
import nl.hamming.storimapp.view.Player;

import java.util.ArrayList;


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
