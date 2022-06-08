package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.request.PlaceThingInRoomDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.Location;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.game.RoomEvent;

public class PlaceThingInRoomAction extends Action<PlaceThingInRoomDTO> {
    private GameController controller;


    public PlaceThingInRoomAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;

    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        UserDto user = client.getCurrentUser();
        PlaceThingInRoomDTO dto = getDto();

        ThingDto thing = client.getServer().getUserDataServerProxy().getThing(dto.getThingId());
        Room room = RoomFactory.getInstance().findRoomByID(dto.getRoomId());
        if ( thing != null ) {
            placeThingInRoom(getClient(), user,thing, room);
        }
    }

    public void placeThingInRoom(ClientConnection source, UserDto user, ThingDto thing, Room room) {
        Location location = new Location(room, room.getSpawnPointX(), room.getSpawnPointY());
        controller.getGameState().setThingLocation(thing, location);
        controller.fireRoomEvent(source, room.getId(), new RoomEvent(RoomEvent.Type.THINGPLACED, thing));
    }


}
