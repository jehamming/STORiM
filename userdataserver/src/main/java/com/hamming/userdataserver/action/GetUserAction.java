package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.GetUserRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.GetUserResultDTO;
import com.hamming.userdataserver.DTOFactory;
import com.hamming.userdataserver.factories.UserFactory;
import com.hamming.userdataserver.model.User;

public class GetUserAction extends Action<GetUserRequestDTO> {

    private ServerWorker serverWorker;

    public GetUserAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        User user = null;
        if ( getDto().getUserId() != null ) {
            user = UserFactory.getInstance().findUserById(getDto().getUserId());
        }

        ProtocolDTO result;
        if ( user != null ) {
            result = new GetUserResultDTO(true, null, DTOFactory.getInstance().getUserDTO(user));
        } else {
            result = new GetUserResultDTO(false, "User "+getDto().getUserId()+" not found!", null);
        }
        getClient().send(result);
    }
}