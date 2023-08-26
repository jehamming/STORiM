package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.protocol.ErrorDTO;
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

        Room room = RoomFactory.getInstance().findRoomByID(dto.getRoomId());
        if ( room != null ) {
            if (client.isAuthorized(room)) {
                if (dto.getName() != null) {
                    room.setName(dto.getName());
                }

                if (dto.getRows() > 0 && dto.getCols() > 0) {
                    room.setSize(dto.getRows(), dto.getCols());
                }

                if (dto.getFrontTileSetId() != null && dto.getFrontTileMap() != null) {
                    room.setFrontTileSetId(dto.getFrontTileSetId());
                    room.setFrontTileMap(dto.getFrontTileMap());
                }

                if (dto.getBackTileSetId() != null && dto.getBackTileMap() != null) {
                    room.setBackTileSetId(dto.getBackTileSetId());
                    room.setBackTileMap(dto.getBackTileMap());
                }
                boolean editable = getClient().isAuthorized(room);
                RoomDto roomDto = DTOFactory.getInstance().getRoomDto(room, client.getServer().getServerURI(), editable);
                RoomUpdatedDTO roomUpdatedDTO = new RoomUpdatedDTO(roomDto);
                getClient().send(roomUpdatedDTO);
                controller.fireRoomEvent(getClient(), room.getId(), new RoomEvent(RoomEvent.Type.ROOMUPDATED, roomDto));
            } else {
                ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), " UnAuthorized");
                client.send(errorDTO);
            }
        } else {
            ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), "Room " + dto.getRoomId() + " not found!");
            client.send(errorDTO);
        }
    }

}
