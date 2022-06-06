package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.request.AddThingDto;
import com.hamming.storim.common.dto.protocol.serverpush.old.ThingAddedDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;

import java.awt.*;

public class AddThingAction extends Action<AddThingDto> {
    private GameController gameController;


    public AddThingAction(GameController controller, STORIMClientConnection client) {
        super(client); this.gameController = controller;

    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        AddThingDto dto = getDto();
        String name = dto.getName();
        String description = dto.getDescription();
        float scale = dto.getScale();
        int rotation = dto.getRotation();
        UserDto creator = client.getCurrentUser();

        ThingDto thing = client.getServer().getUserDataServerProxy().addThing(creator, name, description, scale, rotation, dto.getImageData());
        if ( thing != null ) {
            ThingAddedDTO thingAddedDTO = new ThingAddedDTO(thing);
            client.send(thingAddedDTO);
        }
    }
}
