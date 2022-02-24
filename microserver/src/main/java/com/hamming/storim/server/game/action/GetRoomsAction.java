package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.requestresponse.GetRoomsDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetRoomsResultDTO;
import com.hamming.storim.server.LoginServerConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.Room;

import java.util.HashMap;

public class GetRoomsAction extends Action<GetRoomsDTO> {

    public GetRoomsAction(LoginServerConnection client) {
            super(client);
    }

    @Override
    public void execute() {
        HashMap<Long, String> rooms = new HashMap<>();
        for (Room room : RoomFactory.getInstance().getRooms()) {
            rooms.put(room.getId(), room.getName());
        }
        getClient().send(new GetRoomsResultDTO(rooms));
    }

}
