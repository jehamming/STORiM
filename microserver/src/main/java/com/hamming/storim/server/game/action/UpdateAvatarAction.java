package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.request.UpdateAvatarDto;
import com.hamming.storim.common.dto.protocol.serverpush.AvatarUpdatedDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.UpdateAvatarResponseDTO;
import com.hamming.storim.server.game.GameController;

public class UpdateAvatarAction extends Action<UpdateAvatarDto> {
    private GameController gameController;


    public UpdateAvatarAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.gameController = controller;

    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        UpdateAvatarDto dto = getDto();
        if (dto.getImageData() != null) {
            UpdateAvatarResponseDTO response = client.getServer().getDataServerConnection().updateAvatar(dto.getAvatarId(), dto.getName(), dto.getImageData());
            if (response.isSuccess()) {
                AvatarUpdatedDTO avatarUpdatedDTO = new AvatarUpdatedDTO(response.getAvatar());
                getClient().send(avatarUpdatedDTO);
            } else {
                ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), response.getErrorMessage());
                getClient().send(errorDTO);
            }
        } else {
            ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), "No Imagedata!");
            getClient().send(errorDTO);
        }

    }

}
