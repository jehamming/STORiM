package com.hamming.storim.game.action;

import com.hamming.storim.ClientConnection;
import com.hamming.storim.factories.AvatarFactory;
import com.hamming.storim.game.GameController;
import com.hamming.storim.model.Avatar;
import com.hamming.storim.model.dto.protocol.avatar.DeleteAvatarDTO;

public class DeleteAvatarAction extends Action<DeleteAvatarDTO> {
    private GameController controller;
    private ClientConnection client;

    public DeleteAvatarAction(GameController controller, ClientConnection client) {
        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        DeleteAvatarDTO dto = getDto();
        Avatar avatar = AvatarFactory.getInstance().findAvatarById(dto.getAvatarID());
        if ( avatar != null ) {
            controller.deleteAvatar(avatar);
        }
    }

}
