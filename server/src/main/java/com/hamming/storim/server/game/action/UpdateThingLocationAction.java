package com.hamming.storim.server.game.action;

import com.hamming.storim.server.ClientConnection;
import com.hamming.storim.server.factories.ThingFactory;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.model.Thing;
import com.hamming.storim.common.dto.protocol.thing.UpdateThingLocationDto;

public class UpdateThingLocationAction extends Action<UpdateThingLocationDto> {
    private GameController gameController;
    private ClientConnection client;

    public UpdateThingLocationAction(GameController controller, ClientConnection client) {
        this.gameController = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        UpdateThingLocationDto dto = getDto();
        Thing thing = ThingFactory.getInstance().findThingById(dto.getId());
        if ( thing != null ) {
            gameController.updateThingLocation(thing, dto.getX(), dto.getY());
        }
    }

}
