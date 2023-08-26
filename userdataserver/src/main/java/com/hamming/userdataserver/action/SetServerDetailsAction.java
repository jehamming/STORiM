package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.ServerDetailsDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.ValidateUserRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.ValidateUserResponseDTO;
import com.hamming.userdataserver.DTOFactory;
import com.hamming.userdataserver.Session;
import com.hamming.userdataserver.UserDataClientConnection;
import com.hamming.userdataserver.factories.UserFactory;
import com.hamming.userdataserver.model.User;

public class SetServerDetailsAction extends Action<ServerDetailsDTO> {

    public SetServerDetailsAction(ClientConnection client) {
        super(client);
    }

    @Override
    public void execute() {
        String serverID = getDto().getServerId();
        String oldId = getClient().getId();
        String newId = oldId + "-" + serverID;
        getClient().setId(newId);
    }
}