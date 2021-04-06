package com.hamming.storim.server.game.action;

import com.hamming.storim.server.ClientConnection;
import com.hamming.storim.server.factories.ThingFactory;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.model.Thing;
import com.hamming.storim.common.dto.protocol.thing.DeleteThingDTO;

public class DeleteThingAction extends Action<DeleteThingDTO> {
    private GameController controller;
    private ClientConnection client;

    public DeleteThingAction(GameController controller, ClientConnection client) {
        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        DeleteThingDTO dto = getDto();
        Thing thing  = ThingFactory.getInstance().findThingById(dto.getThingId());
        if ( thing != null ) {
            controller.deleteThing(thing);
        }
    }

}
