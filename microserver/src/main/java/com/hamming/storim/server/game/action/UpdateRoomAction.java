package com.hamming.storim.server.game.action;

import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.common.dto.protocol.room.UpdateRoomDto;

public class UpdateRoomAction extends Action<UpdateRoomDto> {
    private GameController controller;
    private STORIMClientConnection client;

    public UpdateRoomAction(GameController controller, STORIMClientConnection client) {

        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        UpdateRoomDto dto = getDto();
        if ( dto.getImageData() != null ) {
            controller.updateRoom(dto.getRoomId(), dto.getName(), dto.getWidth(), dto.getLength(), dto.getRows(), dto.getCols(), dto.getImageData());
        } else {
            controller.updateRoom(dto.getRoomId(), dto.getName(), dto.getWidth(), dto.getLength(), dto.getRows(), dto.getCols(), dto.getTileId());
        }
    }

}
