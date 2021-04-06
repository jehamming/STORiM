package com.hamming.storim.server.game.action;

import com.hamming.storim.server.ClientConnection;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.common.dto.protocol.avatar.UpdateAvatarDto;
import com.hamming.storim.server.ImageUtils;

import java.awt.*;

public class UpdateAvatarAction extends Action<UpdateAvatarDto> {
    private GameController gameController;
    private ClientConnection client;

    public UpdateAvatarAction(GameController controller, ClientConnection client) {
        this.gameController = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        UpdateAvatarDto dto = getDto();
        if (dto.getImageData() != null ) {
            Image image = ImageUtils.decode(dto.getImageData());
            gameController.updateAvatar(dto.getAvatarId(), dto.getName(), image);
        }
    }

}
