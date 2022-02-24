package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.request.UseExitRequestDTO;
import com.hamming.storim.common.dto.protocol.serverpush.SetCurrentUserDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserLeftRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserLocationUpdatedDTO;
import com.hamming.storim.server.DTOFactory;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
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
        Location location = controller.getGameState().getLocation(currentUser.getId());
        Long fromRoomId = location.getRoom().getId();
        Exit exit = location.getRoom().getExit(getDto().getExitId());
        if (exit != null) {
            // Stop listening to the old Room!
            controller.removeRoomListener(fromRoomId, client);
            Room toRoom = RoomFactory.getInstance().findRoomByID(exit.getRoomid());
            location.setRoom(toRoom);
            location.setX(toRoom.getSpawnPointX());
            location.setY(toRoom.getSpawnPointY());

            Long newRoomId = location.getRoom().getId();
            controller.getGameState().setLocation(currentUser, location);
            client.setRoom(newRoomId);
            // Send current User info
            LocationDto locationDto = DTOFactory.getInstance().getLocationDTO(location);
            UserLocationUpdatedDTO userLocationUpdatedDTO = new UserLocationUpdatedDTO(currentUser.getId(), locationDto, null);
            client.send(userLocationUpdatedDTO);
            // Start listening to the new Room!
            controller.addRoomListener(newRoomId, client);
            // Send other clients an update
            userLeftRoom(getClient(), currentUser, fromRoomId, newRoomId, false);
            //Update userdataserver
            client.updateRoomForUser(currentUser, location);
        }
    }

    public void userLeftRoom(ClientConnection source, UserDto user, Long fromRoomId, Long newRoomId, boolean teleported) {
        controller.fireRoomEvent(source, fromRoomId, new RoomEvent(RoomEvent.Type.USERLEFTROOM, user, newRoomId));
        controller.fireRoomEvent(source, newRoomId, new RoomEvent(RoomEvent.Type.USERENTEREDROOM, user, fromRoomId));
    }


}
