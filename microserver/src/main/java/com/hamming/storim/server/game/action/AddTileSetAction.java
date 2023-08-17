package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.TileSetDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.request.AddAvatarDto;
import com.hamming.storim.common.dto.protocol.request.AddTileSetDto;
import com.hamming.storim.common.dto.protocol.serverpush.AvatarAddedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.TileSetAddedDTO;
import com.hamming.storim.server.DTOFactory;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMException;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.TileSetFactory;
import com.hamming.storim.server.common.model.TileSet;

import java.awt.*;

public class AddTileSetAction extends Action<AddTileSetDto> {

    public AddTileSetAction(STORIMClientConnection client) {
        super(client);
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        AddTileSetDto dto = getDto();
        UserDto creator = client.getCurrentUser();
        if (dto.getImageData() != null) {
            Long cId = creator.getId();
            Image img = ImageUtils.decode(dto.getImageData());
            TileSet tileSet = TileSetFactory.getInstance().createTileSet(cId, dto.getName(), img, dto.getTileWidth(), dto.getTileHeight());
            TileSetDto tileSetDto = DTOFactory.getInstance().getTileSetDTO(tileSet);
            TileSetAddedDTO tileSetAddedDTO = new TileSetAddedDTO(tileSetDto);
            getClient().send(tileSetAddedDTO);

        } else {
            ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), "No Imagedata!");
            getClient().send(errorDTO);
        }
    }

}
