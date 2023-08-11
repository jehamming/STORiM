package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.protocol.request.UpdateRoomDto;
import com.hamming.storim.common.dto.protocol.serverpush.RoomUpdatedDTO;
import com.hamming.storim.server.DTOFactory;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.game.RoomEvent;

public class UpdateRoomAction extends Action<UpdateRoomDto> {
    private GameController controller;

    public UpdateRoomAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        UpdateRoomDto dto = getDto();
        Room updatedRoom = null;

        if ( dto.getName() != null ) {
            updatedRoom = updateRoomName(dto.getRoomId(), dto.getName());
        }

        if ( dto.getRows() > 0 && dto.getCols() > 0  ) {
            updatedRoom = updateRoomSize(dto.getRoomId(), dto.getRows(), dto.getCols());
        }

        if ( dto.getTileSetId() != null && dto.getTileMap() != null  ) {
            updatedRoom = updateRoomTileSet(dto.getRoomId(), dto.getTileSetId(), dto.getTileMap());
        }

        RoomDto roomDto = DTOFactory.getInstance().getRoomDto(updatedRoom, client.getServer().getServerURI());
        RoomUpdatedDTO roomUpdatedDTO = new RoomUpdatedDTO(roomDto);
        getClient().send(roomUpdatedDTO);
        controller.fireRoomEvent(getClient(), updatedRoom.getId(), new RoomEvent(RoomEvent.Type.ROOMUPDATED, roomDto));
    }


    public  Room updateRoomName(Long roomId, String name) {
        Room room = RoomFactory.getInstance().updateRoomName(roomId, name);
        return room;
    }

    public Room updateRoomSize(Long roomId, int rows, int cols) {
        Room room = RoomFactory.getInstance().updateRoomSize(roomId, rows, cols);
        return room;
    }

    public Room updateRoomTileSet(Long roomId, Long tileSetId, int[][] tileMap) {
        Room room = RoomFactory.getInstance().updateRoomTileSet(roomId, tileSetId, tileMap);
        return room;
    }




}
