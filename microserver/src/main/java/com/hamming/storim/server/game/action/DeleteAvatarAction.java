package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.request.DeleteAvatarDTO;
import com.hamming.storim.common.dto.protocol.serverpush.AvatarDeletedDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMException;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.DeleteAvatarResponseDTO;
import com.hamming.storim.server.game.GameController;

public class DeleteAvatarAction extends Action<DeleteAvatarDTO> {
    private GameController controller;


    public DeleteAvatarAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;

    }

    @Override
    public void execute() {
        DeleteAvatarDTO dto = getDto();
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        try {
            client.getServer().getUserDataServerProxy().deleteAvatar(dto.getAvatarID());
            AvatarDeletedDTO avatarDeletedDTO = new AvatarDeletedDTO(dto.getAvatarID());
            getClient().send(avatarDeletedDTO);
        } catch (STORIMException e) {
            ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), e.getMessage());
            client.send(errorDTO);
        }
    }

}
