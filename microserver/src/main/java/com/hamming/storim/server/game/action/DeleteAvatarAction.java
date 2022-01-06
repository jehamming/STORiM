package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.avatar.DeleteAvatarDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMMicroServer;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.AvatarFactory;
import com.hamming.storim.server.common.model.Avatar;
import com.hamming.storim.server.game.GameController;

public class DeleteAvatarAction extends Action<DeleteAvatarDTO> {
    private GameController controller;
    private STORIMClientConnection client;

    public DeleteAvatarAction(GameController controller, STORIMClientConnection client) {

        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        DeleteAvatarDTO dto = getDto();
        Avatar avatar = AvatarFactory.getInstance(STORIMMicroServer.DATADIR).findAvatarById(dto.getAvatarID());
        if ( avatar != null ) {
            controller.deleteAvatar(avatar);
        }
    }

}
