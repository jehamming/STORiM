package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.request.DeleteThingDTO;
import com.hamming.storim.common.dto.protocol.serverpush.ThingDeletedDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMException;
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
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        try {
            client.getServer().getUserDataServerProxy().deleteThing(dto.getThingId());
            ThingDeletedDTO thingDeletedDTO = new ThingDeletedDTO(dto.getThingId());
            getClient().send(thingDeletedDTO);
        } catch (STORIMException e) {
            ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), e.getMessage());
            getClient().send(errorDTO);
        }
    }

}
