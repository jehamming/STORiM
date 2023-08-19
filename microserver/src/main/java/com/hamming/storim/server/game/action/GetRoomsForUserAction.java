package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.requestresponse.GetRoomsForUserDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetRoomsForUserResponseDTO;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.Room;

import java.util.HashMap;

public class GetRoomsForUserAction extends Action<GetRoomsForUserDTO> {

    public GetRoomsForUserAction(ClientConnection client) {
            super(client);
    }

    @Override
    public void execute() {
        HashMap<Long, String> rooms = new HashMap<>();
        for (Room room : RoomFactory.getInstance().getRooms()) {
            Long userId = getDto().getUserId();
            if ( room.getOwnerId().equals( userId) || room.getEditors().contains(userId)) {
                rooms.put(room.getId(), room.getName());
            }
        }
        getClient().send(new GetRoomsForUserResponseDTO(true, rooms, null));
    }

}
