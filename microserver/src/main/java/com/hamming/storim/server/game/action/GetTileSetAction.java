package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.DTO;
import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.TileSetDto;
import com.hamming.storim.common.dto.protocol.requestresponse.GetTileDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetTileResultDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetTileSetDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetTileSetResultDTO;
import com.hamming.storim.server.DTOFactory;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.TileSetFactory;
import com.hamming.storim.server.common.model.TileSet;

public class GetTileSetAction extends Action<GetTileSetDTO> {

    public GetTileSetAction(ClientConnection client) {
            super(client);
    }

    @Override
    public void execute() {
        Long tileId = getDto().getTileSetId();
        boolean success = false;
        String errorMessage = null;

        STORIMClientConnection client = (STORIMClientConnection) getClient();
        TileSet set = TileSetFactory.getInstance().findTileSetById(tileId);
        TileSetDto tileSetDto = null;
        if ( set != null ) {
            tileSetDto = DTOFactory.getInstance().getTileSetDTO(set);
            success = true;
        } else {
            errorMessage = "No Tileset found with tileId " + tileId;
        }
        GetTileSetResultDTO getTileSetResultDTO = new GetTileSetResultDTO(success, tileSetDto, errorMessage);
        getClient().send(getTileSetResultDTO);
    }

}
