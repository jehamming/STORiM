package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.protocol.requestresponse.GetAvatarDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetAvatarResponseDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;

public class GetAvatarAction extends Action<GetAvatarDTO> {

    public GetAvatarAction(ClientConnection client) {
            super(client);
    }

    @Override
    public void execute() {
        Long avatarId = getDto().getAvatarId();

        STORIMClientConnection client = (STORIMClientConnection) getClient();
        AvatarDto avatarDto = client.getServer().getUserDataServerProxy().getAvatar(avatarId);
        GetAvatarResponseDTO response = new GetAvatarResponseDTO(avatarDto);
        getClient().send(response);
    }

}
