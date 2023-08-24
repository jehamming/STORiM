package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.protocol.request.AddRoomDto;
import com.hamming.storim.common.dto.protocol.serverpush.RoomAddedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.TileAddedDTO;
import com.hamming.storim.server.DTOFactory;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.game.GameController;

public class AddRoomAction extends Action<AddRoomDto> {
    private GameController gameController;


    public AddRoomAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.gameController = controller;
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        AddRoomDto dto = getDto();
        Long creator = client.getCurrentUser().getId();

        Room newRoom = client.getServer().addRoom(creator, dto.getName());
        RoomDto roomDto = DTOFactory.getInstance().getRoomDto(newRoom, client.getServer().getServerURI(), client.isAuthorized(newRoom));
        RoomAddedDTO roomAddedDTO = new RoomAddedDTO(roomDto);
        client.send(roomAddedDTO);
    }

}
