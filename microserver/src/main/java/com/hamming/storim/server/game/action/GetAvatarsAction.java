package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.requestresponse.GetAvatarsDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetAvatarsResponseDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;

import java.util.List;

public class GetAvatarsAction extends Action<GetAvatarsDTO> {

    public GetAvatarsAction(ClientConnection client) {
            super(client);
    }

    @Override
    public void execute() {
        Long userId = getDto().getUserId();

        STORIMClientConnection client = (STORIMClientConnection) getClient();
        List<Long> avatarIds = client.getServer().getUserDataServerProxy().getAvatars(userId);

        GetAvatarsResponseDTO response = new GetAvatarsResponseDTO(avatarIds);
        getClient().send(response);
    }

}
