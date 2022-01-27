package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.request.PlaceThingInRoomRequestDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMMicroServer;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.factories.ThingFactory;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.common.model.Thing;
import com.hamming.storim.server.common.model.User;
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
        User user = client.getCurrentUser();
        PlaceThingInRoomRequestDTO dto = getDto();
        Thing thing  = ThingFactory.getInstance(STORIMMicroServer.DATADIR).findThingById(dto.getThingId());
        Room room = RoomFactory.getInstance().findRoomByID(dto.getRoomId());
        if ( thing != null ) {
            controller.placeThingInRoom(getClient(), user,thing, room);
        }
    }

}
