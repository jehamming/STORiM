package com.hamming.storim.server.game.action;

import com.hamming.storim.server.ClientConnection;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.model.User;
import com.hamming.storim.common.dto.protocol.room.AddRoomDto;

public class AddRoomAction extends Action<AddRoomDto> {
    private GameController gameController;
    private ClientConnection client;

    public AddRoomAction(GameController controller, ClientConnection client) {
        this.gameController = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        AddRoomDto dto = getDto();
        User creator = client.getCurrentUser();
        if (dto.getImageData() != null ) {
            gameController.addRoom(creator, dto.getName(), dto.getSize(), dto.getImageData());
        } else if (dto.getTileId() != null ) {
            gameController.addRoom(creator, dto.getName(), dto.getSize(), dto.getTileId());
        } else {
            gameController.addRoom(creator, dto.getName(), dto.getSize());
        }
    }

}
