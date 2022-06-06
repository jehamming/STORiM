package com.hamming.userdataserver.action;

import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.tile.GetThingsForUserRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.tile.GetThingsForUserResponseDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.tile.GetTilesForUserRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.tile.GetTilesForUserResponseDTO;
import com.hamming.userdataserver.factories.ThingFactory;
import com.hamming.userdataserver.factories.TileFactory;
import com.hamming.userdataserver.factories.UserFactory;
import com.hamming.userdataserver.model.Thing;
import com.hamming.userdataserver.model.Tile;
import com.hamming.userdataserver.model.User;

import java.util.ArrayList;
import java.util.List;

public class GetThingsForUserAction extends Action<GetThingsForUserRequestDTO> {

    private ServerWorker serverWorker;

    public GetThingsForUserAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        Long userId = getDto().getUserId();
        List<Long> thingIds = new ArrayList<>();

        List<Thing> things = ThingFactory.getInstance().getThings(userId);
        if (things != null) {
            for (Thing thing: things) {
                thingIds.add(thing.getId());
            }
        }

        GetThingsForUserResponseDTO response = new GetThingsForUserResponseDTO(true, null, thingIds);
        getClient().send(response);
    }

}
