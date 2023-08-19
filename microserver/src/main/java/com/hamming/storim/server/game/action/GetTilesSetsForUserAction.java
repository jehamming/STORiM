package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetTileSetsForUserDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetTileSetsForUserResponseDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetTilesForUserDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetTilesForUserResponseDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMException;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.TileSetFactory;
import com.hamming.storim.server.common.model.TileSet;

import java.util.ArrayList;
import java.util.List;

public class GetTilesSetsForUserAction extends Action<GetTileSetsForUserDTO> {

    public GetTilesSetsForUserAction(ClientConnection client) {
        super(client);
    }

    @Override
    public void execute() {
        List<Long> tileSetIds = new ArrayList<>();
        Long userId = getDto().getUserId();
        List<TileSet> tileSets = TileSetFactory.getInstance().getAllTileSets();
        for (TileSet ts : tileSets) {
            if (ts.getOwnerId().equals( userId) || ts.getEditors().contains(userId) || getClient().isAdmin()) {
                tileSetIds.add(ts.getId());
            }
        }
        getClient().send(new GetTileSetsForUserResponseDTO(true, tileSetIds, null));
    }
}
