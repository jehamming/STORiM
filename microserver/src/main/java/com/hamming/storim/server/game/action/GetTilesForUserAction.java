package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetTilesForUserDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetTilesForUserResponseDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMException;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;

import java.util.List;

public class GetTilesForUserAction extends Action<GetTilesForUserDTO> {

    public GetTilesForUserAction(ClientConnection client) {
        super(client);
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        try {
            List<Long> tiles = client.getServer().getUserDataServerProxy().getTilesForUser(getDto().getUserId());
            getClient().send(new GetTilesForUserResponseDTO(true, tiles, null));
        } catch (STORIMException e) {
            ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), e.getMessage());
            client.send(errorDTO);
        }
    }

}
