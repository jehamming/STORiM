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

import java.net.URI;

public class UseExitAction extends Action<UseExitRequestDTO> {
    private GameController controller;

    public UseExitAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        Exit exit = ExitFactory.getInstance().findExitById(getDto().getExitId());
        if (exit != null) {
            if ( exit.getToRoomURI() != null && !exit.getToRoomURI().equals("") ) {
                connectToOtherServer(exit);
            } else {
                moveToRoomOnThisServer(exit, client);
            }
        }
    }

    private void connectToOtherServer(Exit exit) {
        //TODO Decompose the URI
        String roomURIstr = exit.getToRoomURI();
        URI roomURI =  URI.create(roomURIstr);
    }

    private void moveToRoomOnThisServer(Exit exit, STORIMClientConnection client) {
        UserDto currentUser = client.getCurrentUser();
        Location location = controller.getGameState().getUserLocation(currentUser.getId());
        Room currentRoom = client.getCurrentRoom();
        Long fromRoomId = currentRoom.getId();

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
        LocationUpdateDTO locationUpdateDTO = new LocationUpdateDTO(LocationUpdateDTO.Type.USER, currentUser.getId(), locationDto);
        client.send(locationUpdateDTO);
        // Start listening to the new Room!
        controller.addRoomListener(newRoomId, client);
        // Send other clients an update
        controller.fireRoomEvent(client, fromRoomId, new RoomEvent(RoomEvent.Type.USERLEFTROOM, currentUser, newRoomId));
        controller.fireRoomEvent(client, newRoomId, new RoomEvent(RoomEvent.Type.USERENTEREDROOM, currentUser, fromRoomId));
        //Update userdataserver
        client.getServer().getUserDataServerProxy().setLocation(currentUser.getId(),locationDto);
    }



}
