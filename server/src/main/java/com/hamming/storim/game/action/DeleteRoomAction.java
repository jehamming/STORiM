package com.hamming.storim.game.action;

import com.hamming.storim.ClientConnection;
import com.hamming.storim.factories.RoomFactory;
import com.hamming.storim.factories.VerbFactory;
import com.hamming.storim.game.GameController;
import com.hamming.storim.model.Room;
import com.hamming.storim.model.Verb;
import com.hamming.storim.model.dto.protocol.DeleteRoomDTO;
import com.hamming.storim.model.dto.protocol.DeleteVerbDTO;

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
