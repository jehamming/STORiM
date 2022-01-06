package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.room.DeleteRoomDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.game.GameController;

public class DeleteRoomAction extends Action<DeleteRoomDTO> {
    private GameController controller;
    private STORIMClientConnection client;

    public DeleteRoomAction(GameController controller, STORIMClientConnection client) {

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
