package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.request.UpdateThingLocationDto;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMMicroServer;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.ThingFactory;
import com.hamming.storim.server.common.model.Thing;
import com.hamming.storim.server.game.GameController;

public class UpdateThingLocationAction extends Action<UpdateThingLocationDto> {
    private GameController gameController;
    private STORIMClientConnection client;

    public UpdateThingLocationAction(GameController controller, STORIMClientConnection client) {

        this.gameController = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        UpdateThingLocationDto dto = getDto();
        Thing thing = ThingFactory.getInstance(STORIMMicroServer.DATADIR).findThingById(dto.getId());
        if ( thing != null ) {
            gameController.updateThingLocation(thing, dto.getX(), dto.getY());
        }
    }

}
