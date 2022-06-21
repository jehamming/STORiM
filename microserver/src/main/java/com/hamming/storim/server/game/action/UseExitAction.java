package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.request.UseExitRequestDTO;
import com.hamming.storim.common.dto.protocol.serverpush.LocationUpdateDTO;
import com.hamming.storim.server.DTOFactory;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.ExitFactory;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.Exit;
import com.hamming.storim.server.common.model.Location;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.game.RoomEvent;

public class UseExitAction extends Action<UseExitRequestDTO> {
    private GameController controller;

    public UseExitAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        UserDto currentUser = client.getCurrentUser();
        Location location = controller.getGameState().getUserLocation(currentUser.getId());
        Room currentRoom = client.getCurrentRoom();
        Long fromRoomId = currentRoom.getId();
        Exit exit = ExitFactory.getInstance().findExitById(getDto().getExitId());
        if (exit != null) {
            // Stop listening to the old Room!
            controller.removeRoomListener(fromRoomId, client);
            Room toRoom = RoomFactory.getInstance().findRoomByID(exit.getToRoomID());
            location.setRoomId(toRoom.getId());
            location.setX(toRoom.getSpawnPointX());
            location.setY(toRoom.getSpawnPointY());

            Long newRoomId = location.getRoomId();
            controller.getGameState().setUserLocation(currentUser, location);
            client.setRoom(newRoomId);
            // Send current User info
            LocationDto locationDto = DTOFactory.getInstance().getLocationDTO(location);
            LocationUpdateDTO locationUpdateDTO = new LocationUpdateDTO(currentUser.getId(), locationDto);
            client.send(locationUpdateDTO);
            // Start listening to the new Room!
            controller.addRoomListener(newRoomId, client);
            // Send other clients an update
            userLeftRoom(getClient(), currentUser, fromRoomId, newRoomId);
            //Update userdataserver
            client.getServer().getUserDataServerProxy().setLocation(currentUser.getId(),locationDto);
        }
    }

    public void userLeftRoom(ClientConnection source, UserDto user, Long fromRoomId, Long newRoomId) {
        controller.fireRoomEvent(source, fromRoomId, new RoomEvent(RoomEvent.Type.USERLEFTROOM, user, newRoomId));
        controller.fireRoomEvent(source, newRoomId, new RoomEvent(RoomEvent.Type.USERENTEREDROOM, user, fromRoomId));
    }


}
