package com.hamming.storim.server.game.action;

import com.hamming.storim.server.ClientConnection;
import com.hamming.storim.server.factories.RoomFactory;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.model.Room;
import com.hamming.storim.common.dto.protocol.room.DeleteRoomDTO;

public class DeleteRoomAction extends Action<DeleteRoomDTO> {
    private GameController controller;
    private ClientConnection client;

    public DeleteRoomAction(GameController controller, ClientConnection client) {
        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        Room room = RoomFactory.getInstance().findRoomByID(getDto().getRoomId());
        boolean success = RoomFactory.getInstance().deleteRoom(getDto().getRoomId());
        if (success) {
            controller.roomDeleted(room);
        }
    }

}
