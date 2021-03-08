package com.hamming.storim.game.action;

import com.hamming.storim.ClientConnection;
import com.hamming.storim.factories.DTOFactory;
import com.hamming.storim.factories.RoomFactory;
import com.hamming.storim.game.GameController;
import com.hamming.storim.model.Room;
import com.hamming.storim.model.User;
import com.hamming.storim.model.dto.RoomDto;
import com.hamming.storim.model.dto.protocol.AddRoomDto;
import com.hamming.storim.model.dto.protocol.GetRoomResultDTO;
import com.hamming.storim.model.dto.protocol.UpdateRoomDto;

public class UpdateRoomAction extends Action<UpdateRoomDto> {
    private GameController controller;
    private ClientConnection client;

    public UpdateRoomAction(GameController controller, ClientConnection client) {
        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        UpdateRoomDto dto = getDto();
        Room room = RoomFactory.getInstance().updateRoom(dto.getRoomId(), dto.getName(), dto.getSize());
        if ( room != null ) {
            RoomDto roomDTO = DTOFactory.getInstance().getRoomDto(room);
            GetRoomResultDTO getRoomResultDTO = DTOFactory.getInstance().getRoomResultDTO(true, null, roomDTO);
            client.send(getRoomResultDTO);
        }
    }

}
