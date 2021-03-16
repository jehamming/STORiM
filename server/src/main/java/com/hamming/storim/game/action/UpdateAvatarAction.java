package com.hamming.storim.game.action;

import com.hamming.storim.ClientConnection;
import com.hamming.storim.game.GameController;
import com.hamming.storim.model.User;
import com.hamming.storim.model.dto.protocol.avatar.AddAvatarDto;
import com.hamming.storim.model.dto.protocol.avatar.UpdateAvatarDto;
import com.hamming.storim.util.ImageUtils;

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
        User creator = client.getCurrentUser();
        if (dto.getImageData() != null ) {
            Image image = ImageUtils.decode(dto.getImageData());
            gameController.updateAvatar(dto.getAvatarId(), dto.getName(), image);
        }
    }

}
