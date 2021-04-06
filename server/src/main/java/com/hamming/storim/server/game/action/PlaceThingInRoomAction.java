package com.hamming.storim.server.game.action;

import com.hamming.storim.server.ClientConnection;
import com.hamming.storim.server.factories.RoomFactory;
import com.hamming.storim.server.factories.ThingFactory;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.model.Room;
import com.hamming.storim.server.model.Thing;
import com.hamming.storim.server.model.User;
import com.hamming.storim.common.dto.protocol.thing.PlaceThingInRoomRequestDTO;

public class PlaceThingInRoomAction extends Action<PlaceThingInRoomRequestDTO> {
    private GameController controller;
    private ClientConnection client;

    public PlaceThingInRoomAction(GameController controller, ClientConnection client) {
        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        User user = client.getCurrentUser();
        PlaceThingInRoomRequestDTO dto = getDto();
        Thing thing  = ThingFactory.getInstance().findThingById(dto.getThingId());
        Room room = RoomFactory.getInstance().findRoomByID(dto.getRoomId());
        if ( thing != null ) {
            controller.placeThingInRoom(user,thing, room);
        }
    }

}
