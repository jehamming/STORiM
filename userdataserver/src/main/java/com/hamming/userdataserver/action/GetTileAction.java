package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.tile.GetTileRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.tile.GetTileResponseDTO;
import com.hamming.userdataserver.DTOFactory;
import com.hamming.userdataserver.factories.TileFactory;
import com.hamming.userdataserver.model.Tile;

public class GetTileAction extends Action<GetTileRequestDTO> {

    private ServerWorker serverWorker;

    public GetTileAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        Long tileId = getDto().getTileId();
        boolean success = false;
        String message = null;
        TileDto tileDto = null;

        Tile tile = TileFactory.getInstance().findTileById(tileId);
        if (tile != null) {
            success = true;
            tileDto = DTOFactory.getInstance().getTileDTO(tile);
        } else {
            message = "Tile " + tileId + " not found!";
        }


        GetTileResponseDTO response = new GetTileResponseDTO(success, message, tileDto);
        getClient().send(response);
    }

}
