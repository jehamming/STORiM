package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.tile.AddThingRequestDto;
import com.hamming.storim.server.common.dto.protocol.dataserver.tile.AddThingResponseDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.tile.AddTileRequestDto;
import com.hamming.storim.server.common.dto.protocol.dataserver.tile.AddTileResponseDTO;
import com.hamming.userdataserver.DTOFactory;
import com.hamming.userdataserver.factories.ThingFactory;
import com.hamming.userdataserver.factories.TileFactory;
import com.hamming.userdataserver.factories.UserFactory;
import com.hamming.userdataserver.model.Thing;
import com.hamming.userdataserver.model.Tile;
import com.hamming.userdataserver.model.User;

public class AddThingAction extends Action<AddThingRequestDto> {
    private ServerWorker serverWorker;


    public AddThingAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        AddThingRequestDto dto = getDto();
        String errorMessage = "";
        ThingDto thingDto = null;
        boolean success = false;

        Long creatorId = getDto().getCreatorId();
        String name = getDto().getName();
        String description = getDto().getDescription();
        float scale = getDto().getScale();
        int rotation = getDto().getRotation();
        byte[] imageData = dto.getImageData();
        User creator =  UserFactory.getInstance().findUserById(creatorId);
        if (creator != null) {
            Thing thing = ThingFactory.getInstance().createThing(creatorId, name, description, scale, rotation, ImageUtils.decode(imageData));
            thingDto = DTOFactory.getInstance().getThingDTO(thing);
            success = true;
        } else {
            errorMessage = dto.getClass().getSimpleName() + ": Creator " + creatorId + " not found!";
        }
        AddThingResponseDTO addThingResponseDTO = new AddThingResponseDTO(success, errorMessage, thingDto);
        getClient().send(addThingResponseDTO);
    }

}
