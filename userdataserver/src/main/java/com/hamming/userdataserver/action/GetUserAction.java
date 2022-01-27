package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.DTOFactory;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.GetUserRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.GetUserResultDTO;
import com.hamming.storim.server.common.model.Location;
import com.hamming.userdataserver.UserFactory;
import com.hamming.storim.server.common.model.User;

public class GetUserAction extends Action<GetUserRequestDTO> {

    private ServerWorker serverWorker;

    public GetUserAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        UserDto request = getDto().getUser();
        User user = null;

        if ( request.getId() != null ) {
            user = UserFactory.getInstance().findUserById(request.getId());
        } else if (request.getUsername() != null ) {
            user = UserFactory.getInstance().findUserByUsername(request.getUsername());
        }

        ProtocolDTO result;
        if ( user != null ) {
            result = new GetUserResultDTO(true, null, DTOFactory.getInstance().getUserDTO(user));
        } else {
            result = new GetUserResultDTO(false, "User "+request+" not found!", null);
        }
        getClient().send(result);
    }
}