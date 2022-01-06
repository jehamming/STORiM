package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.thing.DeleteThingDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMMicroServer;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.ThingFactory;
import com.hamming.storim.server.common.model.Thing;
import com.hamming.storim.server.game.GameController;

public class DeleteThingAction extends Action<DeleteThingDTO> {
    private GameController controller;
    private STORIMClientConnection client;

    public DeleteThingAction(GameController controller, STORIMClientConnection client) {

        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        DeleteThingDTO dto = getDto();
        Thing thing  = ThingFactory.getInstance(STORIMMicroServer.DATADIR).findThingById(dto.getThingId());
        if ( thing != null ) {
            controller.deleteThing(thing);
        }
    }

}
