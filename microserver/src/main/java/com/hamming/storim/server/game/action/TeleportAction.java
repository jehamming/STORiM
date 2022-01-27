package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.request.TeleportRequestDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.UserCache;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.Location;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.common.model.User;
import com.hamming.storim.server.game.GameController;

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

    public void handleTeleportRequest(Long userId, Long roomId) {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        User user = UserCache.getInstance().findUserById(userId);
        Room r = RoomFactory.getInstance().findRoomByID(roomId);
        Location loc;
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
           // LocationDto locationDTO = DTOFactory.getInstance().getLocationDTO(user.getLocation());
           // TeleportResultDTO teleportResultDTO = DTOFactory.getInstance().getTeleportResultDTO(true, null, locationDTO,fromRoomId);
          //  getClient().send(teleportResultDTO);
          //  controller.userTeleported(user, fromRoomId, loc);
            }
         else {
           // TeleportResultDTO teleportResultDTO = DTOFactory.getInstance().getTeleportResultDTO(false, "Failed", null, -1L);
           // getClient().send(teleportResultDTO);
        }
    }

}
