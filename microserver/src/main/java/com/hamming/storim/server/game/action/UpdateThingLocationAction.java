package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.protocol.request.UpdateThingLocationDto;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.game.RoomEvent;

public class UpdateThingLocationAction extends Action<UpdateThingLocationDto> {
    private GameController gameController;


    public UpdateThingLocationAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.gameController = controller;

    }

    @Override
    public void execute() {
        UpdateThingLocationDto dto = getDto();
        STORIMClientConnection client = (STORIMClientConnection) getClient();

        LocationDto locationDto = client.getServer().getUserDataServerProxy().getLocation(dto.getId());
        if ( locationDto != null ) {
            locationDto.setX(dto.getX());
            locationDto.setY(dto.getY());
            client.getServer().getUserDataServerProxy().setLocation(dto.getId(), locationDto );
        }
        gameController.fireRoomEvent(client, locationDto.getRoomId(), new RoomEvent(RoomEvent.Type.THINGLOCATIONUPDATE, locationDto));

    }

}
