package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.TileSetDto;
import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.request.UpdateTileSetDto;
import com.hamming.storim.common.dto.protocol.serverpush.TileSetUpdatedDTO;
import com.hamming.storim.server.DTOFactory;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.TileSetFactory;
import com.hamming.storim.server.common.model.TileSet;

import java.awt.*;

public class UpdateTileSetAction extends Action<UpdateTileSetDto> {

    public UpdateTileSetAction(STORIMClientConnection client) {
        super(client);
    }

    @Override
    public void execute() {
        UpdateTileSetDto dto = getDto();
        Long tileSetId = dto.getId();
        TileSet tileSet = TileSetFactory.getInstance().findTileSetById(tileSetId);
        Image img = ImageUtils.decode(dto.getImageData());
        if ( tileSet != null ) {
            tileSet.setImage(img);
            tileSet.setName(dto.getName());
            tileSet.setTileWidth(dto.getTileWidth());
            tileSet.setTileHeight(dto.getTileHeight());
            TileSetDto tileSetDto = DTOFactory.getInstance().getTileSetDTO(tileSet);
            TileSetUpdatedDTO tileSetUpdatedDTO = new TileSetUpdatedDTO(tileSetDto);
            getClient().send(tileSetUpdatedDTO);
            if ( dto.getEditors() != null ) {
                authorisationChanged(tileSet);
            }
        } else {
            ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), "TileSet " + tileSetId + " not found!");
            getClient().send(errorDTO);
        }
    }

    private void authorisationChanged(TileSet tileSet) {
        java.util.List<Long> oldEditors = tileSet.getEditors();
        tileSet.setEditors(getDto().getEditors());
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        client.getServer().getAuthorisationController().fireAuthorisationChanged(tileSet, oldEditors);
    }

}
