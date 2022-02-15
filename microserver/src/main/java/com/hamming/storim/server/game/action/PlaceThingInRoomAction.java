package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.request.PlaceThingInRoomRequestDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.game.GameController;

public class PlaceThingInRoomAction extends Action<PlaceThingInRoomRequestDTO> {
    private GameController controller;


    public PlaceThingInRoomAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;

    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        UserDto user = client.getCurrentUser();
        PlaceThingInRoomRequestDTO dto = getDto();
        //FIXME Things
//        Thing thing  = ThingFactory.getInstance(STORIMMicroServer.DATADIR).findThingById(dto.getThingId());
//        Room room = RoomFactory.getInstance().findRoomByID(dto.getRoomId());
//        if ( thing != null ) {
//            placeThingInRoom(getClient(), user,thing, room);
//        }
    }

    public void placeThingInRoom(ClientConnection source, UserDto user, ThingDto thing, Room room) {
        //FIXME Things
//        Location location = new Location(room, room.getSpawnPointX(), room.getSpawnPointY());
//        thing.setLocation(location);
//        fireGameStateEvent(source, GameStateEvent.Type.THINGPLACED, thing, user);
    }


}
