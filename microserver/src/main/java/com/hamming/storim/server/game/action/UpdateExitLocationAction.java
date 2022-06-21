package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.protocol.request.UpdateExitLocationDto;
import com.hamming.storim.common.dto.protocol.request.UpdateThingLocationDto;
import com.hamming.storim.common.dto.protocol.serverpush.LocationUpdateDTO;
import com.hamming.storim.common.dto.protocol.serverpush.MessageInRoomDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.ExitFactory;
import com.hamming.storim.server.common.model.Exit;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.game.RoomEvent;

public class UpdateExitLocationAction extends Action<UpdateExitLocationDto> {
    private GameController gameController;


    public UpdateExitLocationAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.gameController = controller;

    }

    @Override
    public void execute() {

        //TODO Ar you even allowed to move this Exit? Access control!

        UpdateExitLocationDto dto = getDto();
        STORIMClientConnection client = (STORIMClientConnection) getClient();

        Exit exit = ExitFactory.getInstance().findExitById(dto.getId());
        LocationDto locationDTO = client.getServer().getUserDataServerProxy().getLocation(dto.getId());

        locationDTO.setX(dto.getX());
        locationDTO.setY(dto.getY());

        // Store in Dataserver (when server restarts)
        // This is only done for Thing updates and User enter/exit/disconnect
        client.getServer().getUserDataServerProxy().setLocation(dto.getId(), dto.getX(), dto.getY());

        // Send updated location
        LocationUpdateDTO locationUpdateDTO = new LocationUpdateDTO(dto.getId(), locationDTO);
        client.send(locationUpdateDTO);

        MessageInRoomDTO messageInRoomDTO = new MessageInRoomDTO(client.getCurrentUser().getId(), MessageInRoomDTO.Type.USER, "You move exit " + exit.getName());
        client.send(messageInRoomDTO);

        gameController.fireRoomEvent(client, locationDTO.getRoomId(), new RoomEvent(RoomEvent.Type.EXITLOCATIONUPDATE, locationDTO, client.getCurrentUser()));

    }


}
