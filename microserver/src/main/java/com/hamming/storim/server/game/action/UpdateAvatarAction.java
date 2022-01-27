package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.request.UpdateAvatarDto;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;

import java.awt.*;

public class UpdateAvatarAction extends Action<UpdateAvatarDto> {
    private GameController gameController;


    public UpdateAvatarAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.gameController = controller;

    }

    @Override
    public void execute() {
        UpdateAvatarDto dto = getDto();
        if (dto.getImageData() != null ) {
            Image image = ImageUtils.decode(dto.getImageData());
            gameController.updateAvatar(getClient(), dto.getAvatarId(), dto.getName(), image);
        }
    }

}
