package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.protocol.requestresponse.GetThingDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetThingResultDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetTileDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetTileResultDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;

public class GetThingAction extends Action<GetThingDTO> {

    public GetThingAction(ClientConnection client) {
            super(client);
    }

    @Override
    public void execute() {
        Long thingID = getDto().getThingID();

        STORIMClientConnection client = (STORIMClientConnection) getClient();
        ThingDto thing = client.getServer().getUserDataServerProxy().getThing(thingID);

        getClient().send(new GetThingResultDTO(true,null, thing));
    }

}
