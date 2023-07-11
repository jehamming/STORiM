package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.requestresponse.GetRoomsDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetRoomsResultDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.game.GameController;

import java.util.HashMap;

public class GetRoomsAction extends Action<GetRoomsDTO> {
    private GameController controller;


    public GetRoomsAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;

    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();

        HashMap<Long, String> rooms = new HashMap<Long, String>();
        for (Room r: RoomFactory.getInstance().getRooms()) {
            rooms.put(r.getId(), r.getName());
        }
        GetRoomsResultDTO getRoomsResultDTO = new GetRoomsResultDTO(rooms);

        client.send(getRoomsResultDTO);
    }

}
