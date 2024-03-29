package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.request.TeleportRequestDTO;
import com.hamming.storim.common.dto.protocol.serverpush.LocationUpdateDTO;
import com.hamming.storim.server.DTOFactory;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.Location;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.game.RoomEvent;

public class TeleportAction extends Action<TeleportRequestDTO> {
    private GameController controller;


    public TeleportAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;

    }

    @Override
    public void execute() {
        handleTeleportRequest(getDto().getUserID(), getDto().getRoomID());
    }

    public void handleTeleportRequest(Long userId, Long newRoomId) {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        UserDto user = controller.getGameState().findUserById(userId);
        Room newRoom = RoomFactory.getInstance().findRoomByID(newRoomId);
        if (user != null && newRoom != null ) {
            if ( client.getCurrentRoom() != null ) {
                // Remove old RoomListener
                controller.removeRoomListener(client.getCurrentRoom().getId(), client);
            }
            Location currentLocation = controller.getGameState().getUserLocation(user.getId());
            Long fromRoomId = currentLocation.getRoomId();
            currentLocation.setRoomId(newRoom.getId());
            currentLocation.setX(newRoom.getSpawnCol());
            currentLocation.setY(newRoom.getSpawnRow());
            controller.getGameState().setUserLocation(user, currentLocation);
            client.setRoom(newRoomId);
            // Send current User info
            LocationDto locationDto = DTOFactory.getInstance().getLocationDTO(currentLocation);
            LocationUpdateDTO locationUpdateDTO = new LocationUpdateDTO(LocationUpdateDTO.Type.USER, user.getId(), locationDto);
            client.send(locationUpdateDTO);
            // Send other clients an update
            userLeftRoom(getClient(), user, fromRoomId, newRoomId, false);
            //Update userdataserver
            client.getServer().getUserDataServerProxy().setLocation(user.getId(),locationDto);
        }
    }

    public void userLeftRoom(ClientConnection source, UserDto user, Long fromRoomId, Long newRoomId, boolean teleported) {
        controller.fireRoomEvent(source, fromRoomId, new RoomEvent(RoomEvent.Type.USERLEFTROOM, user, newRoomId));
        controller.fireRoomEvent(source, newRoomId, new RoomEvent(RoomEvent.Type.USERENTEREDROOM, user, fromRoomId));
    }

}
