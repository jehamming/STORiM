package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.protocol.requestresponse.GetTileDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetTileResultDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;

public class GetTileAction extends Action<GetTileDTO> {

    public GetTileAction(ClientConnection client) {
            super(client);
    }

    @Override
    public void execute() {
        Long tileId = getDto().getTileId();

        STORIMClientConnection client = (STORIMClientConnection) getClient();
        TileDto tile = client.getServer().getUserDataServerProxy().getTile(tileId);

        getClient().send(new GetTileResultDTO(tile));
    }

}
