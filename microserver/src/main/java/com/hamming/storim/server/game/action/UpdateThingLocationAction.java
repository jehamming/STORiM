package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.protocol.request.UpdateThingLocationDto;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;

public class UpdateThingLocationAction extends Action<UpdateThingLocationDto> {
    private GameController gameController;


    public UpdateThingLocationAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.gameController = controller;

    }

    @Override
    public void execute() {
        UpdateThingLocationDto dto = getDto();
        //FIXME Things
//        Thing thing = ThingFactory.getInstance(STORIMMicroServer.DATADIR).findThingById(dto.getId());
//        if ( thing != null ) {
//            updateThingLocation(getClient(), thing, dto.getX(), dto.getY());
//        }
    }

    public void updateThingLocation(ClientConnection source, ThingDto thing, int x, int y) {
        //FIXME Things
//        if (thing != null) {
//            thing.getLocation().setX(x);
//            thing.getLocation().setY(y);
//            fireGameStateEvent(source, GameStateEvent.Type.THINGUPDATED, thing, null);
//        }
    }

}
