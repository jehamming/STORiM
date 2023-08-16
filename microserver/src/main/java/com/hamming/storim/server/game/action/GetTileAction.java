package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.protocol.requestresponse.GetTileDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetTileResultDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMException;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;

public class GetTileAction extends Action<GetTileDTO> {

    public GetTileAction(ClientConnection client) {
            super(client);
    }

    @Override
    public void execute() {
        Long tileId = getDto().getTileId();
        TileDto tile = null;
        String errorMessage = null;
        boolean success = false;

        STORIMClientConnection client = (STORIMClientConnection) getClient();
        try {
            tile = client.getServer().getUserDataServerProxy().getTile(tileId);
            success = true;
        } catch (STORIMException e) {
            errorMessage = e.getMessage();
        }

        getClient().send(new GetTileResultDTO(success, tile, errorMessage));
    }

}
