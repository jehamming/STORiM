package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetUserByUserNameDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetUserByUsernameResultDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetUserDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetUserResultDTO;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.userdataserver.DTOFactory;
import com.hamming.userdataserver.factories.UserFactory;
import com.hamming.userdataserver.model.User;

public class GetUserByUsernameAction extends Action<GetUserByUserNameDTO> {

    private ServerWorker serverWorker;

    public GetUserByUsernameAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        User user = null;
        String errorMessage = null;
        if ( getDto().getUserName() != null ) {
            user = UserFactory.getInstance().findUserByUsername(getDto().getUserName());
        }

        GetUserByUsernameResultDTO result;
        if ( user != null ) {
            result = new GetUserByUsernameResultDTO(true, DTOFactory.getInstance().getUserDTO(user),errorMessage);
        } else {
            errorMessage = "User "+getDto().getUserName()+" not found!";
            result = new GetUserByUsernameResultDTO(false,null, errorMessage);
        }
        getClient().send(result);
    }
}