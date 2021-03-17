package com.hamming.storim.game.action;

import com.hamming.storim.ClientConnection;
import com.hamming.storim.factories.RoomFactory;
import com.hamming.storim.factories.ThingFactory;
import com.hamming.storim.game.GameController;
import com.hamming.storim.model.Room;
import com.hamming.storim.model.Thing;
import com.hamming.storim.model.User;
import com.hamming.storim.model.dto.protocol.thing.DeleteThingDTO;
import com.hamming.storim.model.dto.protocol.thing.PlaceThingInRoomRequestDTO;

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
