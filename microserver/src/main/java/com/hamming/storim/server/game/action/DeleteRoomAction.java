package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.request.DeleteRoomDTO;
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
        Room room = RoomFactory.getInstance().findRoomByID(getDto().getRoomId());
        boolean success = RoomFactory.getInstance().deleteRoom(getDto().getRoomId());
        if (success) {
            //FIXME Send room deleted signal?
            //controller.roomDeleted(getClient(), room);
        }
    }

}
