package com.hamming.storim.server.game.action;

import com.hamming.storim.server.ClientConnection;
import com.hamming.storim.server.factories.AvatarFactory;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.model.Avatar;
import com.hamming.storim.common.dto.protocol.avatar.DeleteAvatarDTO;

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
