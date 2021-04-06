package com.hamming.storim.server.game.action;

import com.hamming.storim.server.ClientConnection;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.common.dto.protocol.room.UpdateRoomDto;

public class UpdateRoomAction extends Action<UpdateRoomDto> {
    private GameController controller;
    private ClientConnection client;

    public UpdateRoomAction(GameController controller, ClientConnection client) {
        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        UpdateRoomDto dto = getDto();
        if ( dto.getImageData() != null ) {
            controller.updateRoom(dto.getRoomId(), dto.getName(), dto.getSize(), dto.getImageData());
        } else {
            controller.updateRoom(dto.getRoomId(), dto.getName(), dto.getSize(), dto.getTileId());
        }
    }

}
