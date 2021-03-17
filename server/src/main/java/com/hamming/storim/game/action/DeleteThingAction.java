package com.hamming.storim.game.action;

import com.hamming.storim.ClientConnection;
import com.hamming.storim.factories.AvatarFactory;
import com.hamming.storim.factories.ThingFactory;
import com.hamming.storim.game.GameController;
import com.hamming.storim.model.Avatar;
import com.hamming.storim.model.Thing;
import com.hamming.storim.model.dto.protocol.avatar.DeleteAvatarDTO;
import com.hamming.storim.model.dto.protocol.thing.DeleteThingDTO;

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
