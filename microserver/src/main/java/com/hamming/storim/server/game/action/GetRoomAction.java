package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.protocol.room.GetRoomDTO;
import com.hamming.storim.common.dto.protocol.room.GetRoomResultDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.dto.DTOFactory;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.game.GameController;

public class GetRoomAction extends Action<GetRoomDTO> {
    private GameController controller;
    private STORIMClientConnection client;

    public GetRoomAction(GameController controller, STORIMClientConnection client) {

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
