package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.VerbDto;
import com.hamming.storim.common.dto.protocol.request.UpdateThingDto;
import com.hamming.storim.common.dto.protocol.serverpush.VerbUpdatedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.old.ThingUpdatedDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;

import java.awt.*;

public class UpdateThingAction extends Action<UpdateThingDto> {
    private GameController gameController;


    public UpdateThingAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.gameController = controller;

    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        UpdateThingDto dto = getDto();

        ThingDto thingDto = client.getServer().getUserDataServerProxy().updateThing(dto.getId(), dto.getName(), dto.getDescription(), dto.getScale(), dto.getRotation(), dto.getImageData());
        if ( thingDto != null) {
            ThingUpdatedDTO thingUpdatedDTO = new ThingUpdatedDTO(thingDto, null);
            getClient().send(thingUpdatedDTO);
        }
    }

}
