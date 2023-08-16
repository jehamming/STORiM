package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetThingDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetThingResultDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetTileDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetTileResultDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMException;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;

public class GetThingAction extends Action<GetThingDTO> {

    public GetThingAction(ClientConnection client) {
        super(client);
    }

    @Override
    public void execute() {
        Long thingID = getDto().getThingID();
        boolean success = false;
        String errorMessage = null;
        ThingDto thing = null;
        STORIMClientConnection client = (STORIMClientConnection) getClient();

        try {
            thing = client.getServer().getUserDataServerProxy().getThing(thingID);
            success = true;
        } catch (STORIMException e) {
            errorMessage = e.getMessage();
        }
        getClient().send(new GetThingResultDTO(success, errorMessage, thing));
    }

}
