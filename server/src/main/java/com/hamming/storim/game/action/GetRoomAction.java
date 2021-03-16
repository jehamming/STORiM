package com.hamming.storim.game.action;

import com.hamming.storim.ClientConnection;
import com.hamming.storim.factories.RoomFactory;
import com.hamming.storim.factories.DTOFactory;
import com.hamming.storim.game.GameController;
import com.hamming.storim.model.Room;
import com.hamming.storim.model.dto.protocol.room.GetRoomDTO;
import com.hamming.storim.model.dto.RoomDto;
import com.hamming.storim.model.dto.protocol.room.GetRoomResultDTO;

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
