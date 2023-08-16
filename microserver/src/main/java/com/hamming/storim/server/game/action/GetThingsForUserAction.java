package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.*;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMException;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.Room;

import java.util.HashMap;
import java.util.List;

public class GetThingsForUserAction extends Action<GetThingsForUserDTO> {

    public GetThingsForUserAction(ClientConnection client) {
        super(client);
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        boolean success = false;
        String errorMessage = null;
        List<Long> things = null;
        try {
            things = client.getServer().getUserDataServerProxy().getThingsForUser(getDto().getUserId());
            success = true;

        } catch (STORIMException e) {
            errorMessage = e.getMessage();
        }

        getClient().send(new GetThingsForUserResponseDTO(success, things, errorMessage));
    }

}
