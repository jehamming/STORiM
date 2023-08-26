package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.request.DeleteAvatarDTO;
import com.hamming.storim.common.dto.protocol.request.DeleteTileSetDTO;
import com.hamming.storim.common.dto.protocol.serverpush.AvatarDeletedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.TileSetDeletedDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMException;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.TileSetFactory;
import com.hamming.storim.server.common.model.TileSet;
import com.hamming.storim.server.game.GameController;

public class DeleteTileSetAction extends Action<DeleteTileSetDTO> {
    private GameController controller;


    public DeleteTileSetAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;

    }

    @Override
    public void execute() {
        DeleteTileSetDTO dto = getDto();
        STORIMClientConnection client = (STORIMClientConnection) getClient();

        TileSet tileSet = TileSetFactory.getInstance().findTileSetById(dto.getId());
        if (tileSet != null) {
            if (client.isAuthorized(tileSet)) {
                if (client.getServer().getDefaultTileSet().getId().equals(dto.getId())) {
                    ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), "TileSet " + dto.getId() + " is the default Server tileset and cannot be deleted");
                    client.send(errorDTO);
                } else {
                    TileSetFactory.getInstance().deleteTileSet(tileSet);
                    TileSetDeletedDTO tileSetDeletedDTO = new TileSetDeletedDTO(tileSet.getId());
                    client.send(tileSetDeletedDTO);
                }
            } else {
                ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), "UnAuthorized");
                client.send(errorDTO);
            }
        } else {
            ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), "TileSet " + dto.getId() + " not found");
            client.send(errorDTO);
        }
    }

}
