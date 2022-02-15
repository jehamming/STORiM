package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.request.UpdateThingDto;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;

import java.awt.*;

public class UpdateThingAction extends Action<UpdateThingDto> {
    private GameController gameController;


    public UpdateThingAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.gameController = controller;

    }

    @Override
    public void execute() {
        UpdateThingDto dto = getDto();
        Image image = ImageUtils.decode(dto.getImageData());
        updateThing(getClient(), dto.getId(), dto.getName(), dto.getDescription(), dto.getScale(), dto.getRotation(), image);
    }


    public void updateThing(ClientConnection source, Long id, String name, String description, float scale, int rotation, Image image) {
        //FIXME THings
//        Thing thing = ThingFactory.getInstance(STORIMMicroServer.DATADIR).findThingById(id);
//        if (thing != null) {
//            thing.setName(name);
//            thing.setDescription(description);
//            thing.setScale(scale);
//            thing.setRotation(rotation);
//            thing.setImage(image);
//            fireGameStateEvent(source, GameStateEvent.Type.THINGUPDATED, thing, null);
//        }

    }

}
