package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.request.AddAvatarDto;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.model.User;
import com.hamming.storim.server.game.GameController;

import java.awt.*;

public class AddAvatarAction extends Action<AddAvatarDto> {
    private GameController gameController;
    private STORIMClientConnection client;

    public AddAvatarAction(GameController controller, STORIMClientConnection client) {
        this.gameController = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        AddAvatarDto dto = getDto();
        User creator = client.getCurrentUser();
        if (dto.getImageData() != null ) {
            Image image = ImageUtils.decode(dto.getImageData());
            gameController.addAvatar(creator.getId(), dto.getName(), image);
        }
    }

}
