package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.protocol.request.DeleteThingDTO;
import com.hamming.storim.common.dto.protocol.serverpush.VerbDeletedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.old.RoomDeletedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.old.ThingDeletedDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.RoomFactory;
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
        boolean success = client.getServer().getUserDataServerProxy().deleteThing(dto.getThingId());
        if (success) {
            ThingDeletedDTO thingDeletedDTO = new ThingDeletedDTO(dto.getThingId());
            getClient().send(thingDeletedDTO);
        }
    }

}
