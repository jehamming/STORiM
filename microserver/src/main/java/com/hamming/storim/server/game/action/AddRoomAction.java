package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.request.AddRoomDto;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;

public class AddRoomAction extends Action<AddRoomDto> {
    private GameController gameController;


    public AddRoomAction(GameController controller, STORIMClientConnection client) {
        super(client); this.gameController = controller;

    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        AddRoomDto dto = getDto();
        Long creator = client.getCurrentUser().getId();
        if (dto.getImageData() != null ) {
            gameController.addRoom(getClient(), creator, dto.getName(), dto.getImageData());
        } else if (dto.getTileId() != null ) {
            gameController.addRoom(getClient(), creator, dto.getName(), dto.getTileId());
        } else {
            gameController.addRoom(getClient(), creator, dto.getName());
        }
    }

}
