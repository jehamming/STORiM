package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.tile.AddTileRequestDto;
import com.hamming.storim.server.common.dto.protocol.dataserver.tile.AddTileResponseDTO;
import com.hamming.userdataserver.DTOFactory;
import com.hamming.userdataserver.factories.TileFactory;
import com.hamming.userdataserver.factories.UserFactory;
import com.hamming.userdataserver.model.Tile;
import com.hamming.userdataserver.model.User;

public class AddTileAction extends Action<AddTileRequestDto> {
    private ServerWorker serverWorker;


    public AddTileAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        AddTileRequestDto dto = getDto();
        String errorMessage = "";
        TileDto tileDto = null;
        boolean success = false;

        Long creatorId = getDto().getUserId();
        User creator =  UserFactory.getInstance().findUserById(creatorId);
        if (creator != null) {
            Tile tile = TileFactory.getInstance().createTile(creatorId, ImageUtils.decode(dto.getImageData()));
            tileDto = DTOFactory.getInstance().getTileDTO(tile);
            success = true;
        } else {
            errorMessage = dto.getClass().getSimpleName() + ": Avatar " + creatorId + " not found!";
        }
        AddTileResponseDTO addTileResponseDTO = new AddTileResponseDTO(success, errorMessage, tileDto);
        getClient().send(addTileResponseDTO);
    }

}
