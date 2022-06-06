package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.tile.GetThingRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.tile.GetThingResponseDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.tile.GetTileRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.tile.GetTileResponseDTO;
import com.hamming.userdataserver.DTOFactory;
import com.hamming.userdataserver.factories.ThingFactory;
import com.hamming.userdataserver.factories.TileFactory;
import com.hamming.userdataserver.model.Thing;
import com.hamming.userdataserver.model.Tile;

public class GetThingAction extends Action<GetThingRequestDTO> {

    private ServerWorker serverWorker;

    public GetThingAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        Long thingId = getDto().getThingID();
        boolean success = false;
        String message = null;
        ThingDto thingDto = null;

        Thing thing = ThingFactory.getInstance().findThingById(thingId);
        if (thing != null) {
            success = true;
            thingDto = DTOFactory.getInstance().getThingDTO(thing);
        } else {
            message = "Thing " + thingId + " not found!";
        }


        GetThingResponseDTO response = new GetThingResponseDTO(success, message, thingDto);
        getClient().send(response);
    }

}
