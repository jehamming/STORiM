package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.protocol.request.AddRoomDto;
import com.hamming.storim.common.dto.protocol.serverpush.RoomAddedDTO;
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

        Room newRoom = addRoom(creator, dto.getName());
        if (dto.getImageData() != null ) {
            TileDto tile = client.getServer().getUserDataServerProxy().addTile(creator, dto.getImageData());
            newRoom.setTileId(tile.getId());
        } else if (dto.getTileId() != null ) {
            TileDto tile = client.getServer().getUserDataServerProxy().getTile(dto.getTileId());
            newRoom.setTileId(tile.getId());
        }

        RoomDto roomDto = DTOFactory.getInstance().getRoomDto(newRoom);
        RoomAddedDTO roomAddedDTO = new RoomAddedDTO(roomDto);
        client.send(roomAddedDTO);
    }


    public Room addRoom(Long creatorId, String name) {
        Room room = RoomFactory.getInstance().createRoom(creatorId, name);
        room.setOwnerId(creatorId);
        return room;
    }


}
