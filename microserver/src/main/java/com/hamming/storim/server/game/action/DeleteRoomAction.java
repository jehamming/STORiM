package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.request.DeleteRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.RoomDeletedDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.game.GameController;

public class DeleteRoomAction extends Action<DeleteRoomDTO> {
    private GameController controller;


    public DeleteRoomAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;

    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        Room defaultRoom = client.getServer().getServerConfiguration().getDefaultRoom();
        Room room = RoomFactory.getInstance().findRoomByID(getDto().getRoomId());
        if ( room != null ) {
            if (client.isAuthorized(room)) {
                if (defaultRoom != null && room.getId().equals(defaultRoom.getId())) {
                    ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), "Room " + getDto().getRoomId() + " is the default Server Room and cannot be deleted");
                    client.send(errorDTO);
                } else {
                    boolean success = RoomFactory.getInstance().deleteRoom(room.getId());
                    if (success) {
                        RoomDeletedDTO roomDeletedDTO = new RoomDeletedDTO(room.getId());
                        getClient().send(roomDeletedDTO);
                    }
                }
            } else {
                ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), "UnAuthorized");
                client.send(errorDTO);
            }
        } else {
            ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), "Room " + getDto().getRoomId() + " not found");
            client.send(errorDTO);
        }
    }

}
