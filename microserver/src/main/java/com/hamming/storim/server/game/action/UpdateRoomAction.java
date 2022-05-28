package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.protocol.request.UpdateRoomDto;
import com.hamming.storim.common.dto.protocol.serverpush.old.RoomUpdatedDTO;
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
        UpdateRoomDto dto = getDto();
        Room updatedRoom = null;
        if ( dto.getImageData() != null ) {
            updatedRoom = updateRoom(dto.getRoomId(), dto.getName(), dto.getWidth(), dto.getLength(), dto.getRows(), dto.getCols(), dto.getImageData());
        } else {
            updatedRoom = updateRoom(dto.getRoomId(), dto.getName(), dto.getWidth(), dto.getLength(), dto.getRows(), dto.getCols(), dto.getTileId());
        }

        RoomDto roomDto = DTOFactory.getInstance().getRoomDto(updatedRoom);
        RoomUpdatedDTO roomUpdatedDTO = new RoomUpdatedDTO(roomDto);
        getClient().send(roomUpdatedDTO);
        controller.fireRoomEvent(getClient(), updatedRoom.getId(), new RoomEvent(RoomEvent.Type.ROOMUPDATED, roomDto));
    }


    public  Room updateRoom(Long roomId, String name, int width, int length, int rows, int cols, Long tileId) {
        Room room = RoomFactory.getInstance().updateRoom(roomId, name, width, length, rows, cols);
        room.setTileId(tileId);
        return room;
    }

    public Room updateRoom(Long roomId, String name, int width, int length, int rows, int cols, byte[] imageData) {
        Room room = RoomFactory.getInstance().updateRoom(roomId, name, width, length, rows, cols);
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        Long creator = client.getCurrentUser().getId();
        TileDto tile = client.getServer().getUserDataServerProxy().addTile(creator, imageData);
        room.setTileId(tile.getId());
        return room;
    }




}
