package com.hamming.storim.server.game.action;

import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.common.dto.protocol.request.UpdateRoomDto;

public class UpdateRoomAction extends Action<UpdateRoomDto> {
    private GameController controller;

    public UpdateRoomAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        UpdateRoomDto dto = getDto();
        if ( dto.getImageData() != null ) {
            controller.updateRoom(getClient(), dto.getRoomId(), dto.getName(), dto.getWidth(), dto.getLength(), dto.getRows(), dto.getCols(), dto.getImageData());
        } else {
            controller.updateRoom(getClient(), dto.getRoomId(), dto.getName(), dto.getWidth(), dto.getLength(), dto.getRows(), dto.getCols(), dto.getTileId());
        }
    }

}
