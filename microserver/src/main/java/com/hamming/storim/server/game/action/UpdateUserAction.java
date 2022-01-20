package com.hamming.storim.server.game.action;

import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.common.dto.protocol.request.UpdateUserDto;

public class UpdateUserAction extends Action<UpdateUserDto> {
    private GameController controller;
    private STORIMClientConnection client;

    public UpdateUserAction(GameController controller, STORIMClientConnection client) {

        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        UpdateUserDto dto = getDto();

        controller.updateUser(dto.getId(), dto.getName(), dto.getEmail(), dto.getAvatarID());

    }

}
