package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.request.UpdateThingDto;
import com.hamming.storim.common.dto.protocol.serverpush.ThingUpdatedDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMException;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.game.RoomEvent;

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

        try {
        ThingDto thingDto = client.getServer().getUserDataServerProxy().updateThing(dto.getId(), dto.getName(), dto.getDescription(), dto.getScale(), dto.getRotation(), dto.getImageData());
        if ( thingDto != null) {
            ThingUpdatedDTO thingUpdatedDTO = new ThingUpdatedDTO(thingDto);
            getClient().send(thingUpdatedDTO);

            LocationDto locationDto = client.getServer().getUserDataServerProxy().getLocation(thingDto.getId());
            if ( locationDto != null ) {
                gameController.fireRoomEvent(client, locationDto.getRoomId(), new RoomEvent(RoomEvent.Type.THINGUPDATED, thingDto, client.getCurrentUser()));
            }
        }
        } catch (STORIMException e) {
            ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), e.getMessage());
            client.send(errorDTO);
        }
    }

}
