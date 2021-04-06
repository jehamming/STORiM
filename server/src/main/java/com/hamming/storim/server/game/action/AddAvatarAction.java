package com.hamming.storim.server.game.action;

import com.hamming.storim.server.ClientConnection;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.model.User;
import com.hamming.storim.common.dto.protocol.avatar.AddAvatarDto;
import com.hamming.storim.server.ImageUtils;

import java.awt.*;

public class AddAvatarAction extends Action<AddAvatarDto> {
    private GameController gameController;
    private ClientConnection client;

    public AddAvatarAction(GameController controller, ClientConnection client) {
        this.gameController = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        AddAvatarDto dto = getDto();
        User creator = client.getCurrentUser();
        if (dto.getImageData() != null ) {
            Image image = ImageUtils.decode(dto.getImageData());
            gameController.addAvatar(creator, dto.getName(), image);
        }
    }

}
