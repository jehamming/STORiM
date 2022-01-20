package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.request.UpdateAvatarDto;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;

import java.awt.*;

public class UpdateAvatarAction extends Action<UpdateAvatarDto> {
    private GameController gameController;
    private STORIMClientConnection client;

    public UpdateAvatarAction(GameController controller, STORIMClientConnection client) {

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
