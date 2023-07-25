package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetUserDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetUserResultDTO;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.userdataserver.DTOFactory;
import com.hamming.userdataserver.factories.UserFactory;
import com.hamming.userdataserver.model.User;

public class GetUserAction extends Action<GetUserDTO> {

    private ServerWorker serverWorker;

    public GetUserAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        User user = null;
        String errorMessage = null;
        if ( getDto().getUserID() != null ) {
            user = UserFactory.getInstance().findUserById(getDto().getUserID());
        }

        ProtocolDTO result;
        if ( user != null ) {
            result = new GetUserResultDTO(true, DTOFactory.getInstance().getUserDTO(user),errorMessage);
        } else {
            errorMessage = "User "+getDto().getUserID()+" not found!";
            result = new GetUserResultDTO(false,null, errorMessage);
        }
        getClient().send(result);
    }
}