package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.request.AddThingDto;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;

import java.awt.*;

public class AddThingAction extends Action<AddThingDto> {
    private GameController gameController;


    public AddThingAction(GameController controller, STORIMClientConnection client) {
        super(client); this.gameController = controller;

    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        AddThingDto dto = getDto();
        UserDto creator = client.getCurrentUser();

        Image image = ImageUtils.decode(dto.getImageData());
        addThing(getClient(), creator.getId(), dto.getName(), dto.getDescription(), dto.getScale(), dto.getRotation(), image);
    }

    public void addThing(ClientConnection source, Long creatorId, String name, String description, float scale, int rotation, Image image) {
        //FIXME THings
//        Thing thing = ThingFactory.getInstance(STORIMMicroServer.DATADIR).createThing(creatorId, name, description, scale, rotation, image);
//        fireGameStateEvent(source, GameStateEvent.Type.THINGADDED, thing, null);
    }

}
