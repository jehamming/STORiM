package com.hamming.storim.server.game.action;

import com.hamming.storim.server.ClientConnection;
import com.hamming.storim.server.factories.RoomFactory;
import com.hamming.storim.server.factories.DTOFactory;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.model.Room;
import com.hamming.storim.common.dto.protocol.room.GetRoomDTO;
import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.protocol.room.GetRoomResultDTO;

public class GetRoomAction extends Action<GetRoomDTO> {
    private GameController controller;
    private ClientConnection client;

    public GetRoomAction(GameController controller, ClientConnection client) {
        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        Room bp = RoomFactory.getInstance().findRoomByID(getDto().getRoomID());
        if ( bp != null ) {
            RoomDto roomDto = DTOFactory.getInstance().getRoomDto(bp);
            GetRoomResultDTO getRoomResultDTO = DTOFactory.getInstance().getRoomResultDTO(true, null, roomDto);
            client.send(getRoomResultDTO);
        } else {
            GetRoomResultDTO getRoomResultDTO = DTOFactory.getInstance().getRoomResultDTO(false, "Room not found!", null);
            client.send(getRoomResultDTO);
        }
    }

}
