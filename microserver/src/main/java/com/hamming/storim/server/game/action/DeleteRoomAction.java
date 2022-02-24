package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.request.DeleteRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.old.RoomDeletedDTO;
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
        boolean success = RoomFactory.getInstance().deleteRoom(getDto().getRoomId());
        if (success) {
            RoomDeletedDTO roomDeletedDTO = new RoomDeletedDTO(getDto().getRoomId());
            getClient().send(roomDeletedDTO);
        }
    }

}
