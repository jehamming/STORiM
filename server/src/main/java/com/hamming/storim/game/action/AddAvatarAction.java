package com.hamming.storim.game.action;

import com.hamming.storim.ClientConnection;
import com.hamming.storim.game.GameController;
import com.hamming.storim.model.User;
import com.hamming.storim.model.dto.protocol.avatar.AddAvatarDto;
import com.hamming.storim.util.ImageUtils;

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
