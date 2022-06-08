package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.request.PlaceThingInRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.MessageInRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.old.ThingInRoomDTO;
import com.hamming.storim.server.DTOFactory;
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
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        String serverName = client.getServer().getServerName();
        Location location = new Location(thing.getId(), serverName, room.getId(),room.getSpawnPointX(), room.getSpawnPointY());
        LocationDto locationDto = DTOFactory.getInstance().getLocationDTO(location);
        //Store location at dataserver
        client.getServer().getUserDataServerProxy().setLocation(thing.getId(), locationDto);
        room.addObjectInRoom(thing.getId());

        ThingInRoomDTO thingInRoomDTO = new ThingInRoomDTO(thing, locationDto );
        client.send(thingInRoomDTO);

        String toCaller = "You place " + thing.getName();
        MessageInRoomDTO messageInRoomDTO = new MessageInRoomDTO(user.getId(), MessageInRoomDTO.Type.USER, toCaller);
        client.send(messageInRoomDTO);

        controller.fireRoomEvent(source, room.getId(), new RoomEvent(RoomEvent.Type.THINGPLACED, thing, user));

    }


}
