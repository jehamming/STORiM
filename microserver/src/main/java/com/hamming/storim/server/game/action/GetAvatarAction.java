package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.protocol.requestresponse.GetAvatarDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetAvatarResponseDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMException;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;

public class GetAvatarAction extends Action<GetAvatarDTO> {

    public GetAvatarAction(ClientConnection client) {
            super(client);
    }

    @Override
    public void execute() {
        Long avatarId = getDto().getAvatarId();
        AvatarDto avatarDto = null;
        boolean success = false;
        String errorMessage = null;

        STORIMClientConnection client = (STORIMClientConnection) getClient();
        try {
            avatarDto = client.getServer().getUserDataServerProxy().getAvatar(avatarId);
            success = true;
        } catch (STORIMException e) {
            errorMessage = e.getMessage();
        }
        GetAvatarResponseDTO response = new GetAvatarResponseDTO(success, avatarDto, errorMessage);
        getClient().send(response);
    }

}
