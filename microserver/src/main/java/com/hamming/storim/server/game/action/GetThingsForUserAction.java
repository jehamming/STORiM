package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.requestresponse.*;
import com.hamming.storim.server.STORIMClientConnection;
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
        List<Long> things = client.getServer().getUserDataServerProxy().getThingsForUser(getDto().getUserId());
        getClient().send(new GetThingsForUserResponseDTO(things));
    }

}
