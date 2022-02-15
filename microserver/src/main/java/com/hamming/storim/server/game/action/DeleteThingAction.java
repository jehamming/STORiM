package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.protocol.request.DeleteThingDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;

public class DeleteThingAction extends Action<DeleteThingDTO> {
    private GameController controller;


    public DeleteThingAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;

    }

    @Override
    public void execute() {
        DeleteThingDTO dto = getDto();
        //FIXME Things
//        ThingDto thing  = ThingFactory.getInstance(STORIMMicroServer.DATADIR).findThingById(dto.getThingId());
//        if ( thing != null ) {
//            controller.deleteThing(getClient(), thing);
//        }
    }

    public void deleteThing(ClientConnection source, ThingDto thing) {
        //FIXME Things
//        ThingFactory.getInstance(STORIMMicroServer.DATADIR).deleteThing(thing);
//        fireGameStateEvent(source, GameStateEvent.Type.THINGDELETED, thing, null);
    }

}
