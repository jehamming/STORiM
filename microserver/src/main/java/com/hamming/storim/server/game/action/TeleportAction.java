package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.request.TeleportRequestDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserLocationUpdatedDTO;
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
        Location loc;
        if (user != null && newRoom != null ) {
            Location currentLocation = controller.getGameState().getUserLocation(user.getId());
            Long fromRoomId = currentLocation.getRoomId();
            currentLocation.setRoomId(newRoom.getId());
            currentLocation.setX(newRoom.getSpawnPointX());
            currentLocation.setY(newRoom.getSpawnPointY());
            controller.getGameState().setUserLocation(user, currentLocation);
            client.setRoom(newRoomId);
            // Send current User info
            LocationDto locationDto = DTOFactory.getInstance().getLocationDTO(currentLocation);
            UserLocationUpdatedDTO userLocationUpdatedDTO = new UserLocationUpdatedDTO(user.getId(), locationDto, null);
            client.send(userLocationUpdatedDTO);
            // Start listening to the new Room!
            controller.addRoomListener(newRoomId, client);
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
