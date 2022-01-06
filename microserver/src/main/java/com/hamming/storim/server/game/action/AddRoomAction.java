package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.room.AddRoomDto;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;

public class AddRoomAction extends Action<AddRoomDto> {
    private GameController gameController;
    private STORIMClientConnection client;

    public AddRoomAction(GameController controller, STORIMClientConnection client) {
        this.gameController = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        AddRoomDto dto = getDto();
        Long creator = client.getCurrentUser().getId();
        if (dto.getImageData() != null ) {
            gameController.addRoom(creator, dto.getName(), dto.getImageData());
        } else if (dto.getTileId() != null ) {
            gameController.addRoom(creator, dto.getName(), dto.getTileId());
        } else {
            gameController.addRoom(creator, dto.getName());
        }
    }

}
