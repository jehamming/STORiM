package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.requestresponse.GetTileSetsDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetTileSetsResponseDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetTilesForUserResponseDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.TileSetFactory;
import com.hamming.storim.server.common.model.TileSet;

import java.util.ArrayList;
import java.util.List;

public class GetTileSetsAction extends Action<GetTileSetsDTO> {

    public GetTileSetsAction(ClientConnection client) {
            super(client);
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        List<TileSet> tileSets = TileSetFactory.getInstance().getAllTileSets();
        List<Long> tileSetIds = new ArrayList<>();
        for (TileSet tileSet :tileSets) {
            tileSetIds.add(tileSet.getId());
        }
        getClient().send(new GetTileSetsResponseDTO(tileSetIds));
    }

}
