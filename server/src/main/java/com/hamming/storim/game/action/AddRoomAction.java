package com.hamming.storim.game.action;

import com.hamming.storim.ClientConnection;
import com.hamming.storim.factories.DTOFactory;
import com.hamming.storim.factories.RoomFactory;
import com.hamming.storim.factories.VerbFactory;
import com.hamming.storim.game.GameController;
import com.hamming.storim.model.Room;
import com.hamming.storim.model.User;
import com.hamming.storim.model.Verb;
import com.hamming.storim.model.dto.RoomDto;
import com.hamming.storim.model.dto.VerbDto;
import com.hamming.storim.model.dto.protocol.AddRoomDto;
import com.hamming.storim.model.dto.protocol.AddVerbDto;
import com.hamming.storim.model.dto.protocol.GetRoomResultDTO;
import com.hamming.storim.model.dto.protocol.GetVerbResultDTO;

public class AddRoomAction extends Action<AddRoomDto> {
    private GameController controller;
    private ClientConnection client;

    public AddRoomAction(GameController controller, ClientConnection client) {
        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        AddRoomDto dto = getDto();
        User creator = client.getCurrentUser();
        Room room = RoomFactory.getInstance().createRoom(creator, dto.getName(), dto.getSize());
        if ( room != null ) {
            creator.addRoom(room);
            RoomDto roomDTO = DTOFactory.getInstance().getRoomDto(room);
            GetRoomResultDTO getRoomResultDTO = DTOFactory.getInstance().getRoomResultDTO(true, null, roomDTO);
            client.send(getRoomResultDTO);
        }
    }

}
