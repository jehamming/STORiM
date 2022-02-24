package com.hamming.userdataserver.action;

import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.tile.GetTilesForUserRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.tile.GetTilesForUserResponseDTO;
import com.hamming.userdataserver.factories.TileFactory;
import com.hamming.userdataserver.factories.UserFactory;
import com.hamming.userdataserver.model.Tile;
import com.hamming.userdataserver.model.User;

import java.util.ArrayList;
import java.util.List;

public class GetTilesForUserAction extends Action<GetTilesForUserRequestDTO> {

    private ServerWorker serverWorker;

    public GetTilesForUserAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        Long userId = getDto().getUserId();
        List<Long> tileIds = new ArrayList<>();

        User user = UserFactory.getInstance().findUserById(userId);
        List<Tile> tiles = TileFactory.getInstance().geTiles(user);
        if (tiles != null) {
            for (Tile tile: tiles) {
                tileIds.add(tile.getId());
            }
        }

        GetTilesForUserResponseDTO response = new GetTilesForUserResponseDTO(true, null, tileIds);
        getClient().send(response);
    }

}
