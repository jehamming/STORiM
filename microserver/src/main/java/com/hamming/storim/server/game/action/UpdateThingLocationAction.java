package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.protocol.request.UpdateThingLocationDto;
import com.hamming.storim.common.dto.protocol.serverpush.LocationUpdateDTO;
import com.hamming.storim.common.dto.protocol.serverpush.MessageInRoomDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.model.Location;
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

        //TODO Ar you even allowed to move this thing? Access control!

        UpdateThingLocationDto dto = getDto();
        STORIMClientConnection client = (STORIMClientConnection) getClient();

        ThingDto thingDto = client.getServer().getUserDataServerProxy().getThing(dto.getId());

        LocationDto locationDTO = client.getServer().getUserDataServerProxy().getLocation(dto.getId());
        locationDTO.setX(dto.getX());
        locationDTO.setY(dto.getY());

        // Store in Dataserver (when server restarts)
        // This is only done for Thing updates and User enter/exit/disconnect
        client.getServer().getUserDataServerProxy().setLocation(dto.getId(), dto.getX(), dto.getY());

        // Send updated location
        LocationUpdateDTO locationUpdateDTO = new LocationUpdateDTO(LocationUpdateDTO.Type.THING, dto.getId(), locationDTO);
        client.send(locationUpdateDTO);

        MessageInRoomDTO messageInRoomDTO = new MessageInRoomDTO(client.getCurrentUser().getId(), MessageInRoomDTO.sType.USER, "You move " + thingDto.getName(), MessageInRoomDTO.mType.MOVE);
        client.send(messageInRoomDTO);

        gameController.fireRoomEvent(client, locationDTO.getRoomId(), new RoomEvent(RoomEvent.Type.THINGLOCATIONUPDATE, locationDTO, client.getCurrentUser()));

    }


}
