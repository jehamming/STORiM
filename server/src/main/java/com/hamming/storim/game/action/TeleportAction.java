package com.hamming.storim.game.action;

import com.hamming.storim.ClientConnection;
import com.hamming.storim.factories.DTOFactory;
import com.hamming.storim.factories.RoomFactory;
import com.hamming.storim.factories.UserFactory;
import com.hamming.storim.game.GameController;
import com.hamming.storim.model.Location;
import com.hamming.storim.model.Room;
import com.hamming.storim.model.User;
import com.hamming.storim.model.dto.protocol.TeleportRequestDTO;
import com.hamming.storim.model.dto.LocationDto;
import com.hamming.storim.model.dto.protocol.TeleportResultDTO;

public class TeleportAction extends Action<TeleportRequestDTO> {
    private GameController controller;
    private ClientConnection client;

    public TeleportAction(GameController controller, ClientConnection client) {
        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        handleTeleportRequest(getDto().getUserID(), getDto().getRoomID());
    }

    public void handleTeleportRequest(Long userId, Long roomId) {
        User user = UserFactory.getInstance().findUserById(userId);
        Room r = RoomFactory.getInstance().findRoomByID(roomId);
        Location loc = null;
        if (user != null && r != null ) {
            Long fromRoomId = user.getLocation().getRoom().getId();
            loc = user.getLocation();
            if (loc == null) {
                loc = new Location(r, r.getSpawnPointX(), r.getSpawnPointY());
                user.setLocation(loc);
            } else {
                loc.setRoom(r);
                loc.setX(r.getSpawnPointX());
                loc.setY(r.getSpawnPointY());
            }

            client.sendRoom(user.getLocation().getRoom());
            LocationDto locationDTO = DTOFactory.getInstance().getLocationDTO(user.getLocation());
            TeleportResultDTO teleportResultDTO = DTOFactory.getInstance().getTeleportResultDTO(true, null, locationDTO,fromRoomId);
            client.send(teleportResultDTO);
            controller.userTeleported(user, fromRoomId, loc);
            }
         else {
            TeleportResultDTO teleportResultDTO = DTOFactory.getInstance().getTeleportResultDTO(false, "Failed", null, -1L);
            client.send(teleportResultDTO);
        }
    }

}
