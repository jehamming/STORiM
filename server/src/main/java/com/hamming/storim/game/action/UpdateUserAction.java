package com.hamming.storim.game.action;

import com.hamming.storim.ClientConnection;
import com.hamming.storim.game.GameController;
import com.hamming.storim.model.dto.protocol.user.UpdateUserDto;

public class UpdateUserAction extends Action<UpdateUserDto> {
    private GameController controller;
    private ClientConnection client;

    public UpdateUserAction(GameController controller, ClientConnection client) {
        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        UpdateUserDto dto = getDto();

        controller.updateUser(dto.getId(), dto.getName(), dto.getEmail(), dto.getAvatarID());

    }

}
