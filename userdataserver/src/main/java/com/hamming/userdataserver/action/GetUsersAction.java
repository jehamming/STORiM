package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.protocol.requestresponse.GetUsersRequestDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetUsersResultDTO;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.GetVerbsRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.GetVerbsResponseDTO;
import com.hamming.userdataserver.factories.UserFactory;
import com.hamming.userdataserver.factories.VerbFactory;
import com.hamming.userdataserver.model.User;
import com.hamming.userdataserver.model.Verb;

import java.util.HashMap;
import java.util.List;

public class GetUsersAction extends Action<GetUsersRequestDTO> {

    private ServerWorker serverWorker;

    public GetUsersAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        HashMap<Long, String> users = new HashMap<>();
        for (User user: UserFactory.getInstance().getUsers()) {
            if ( !user.equals( UserFactory.getInstance().getRootUser())) {
                users.put(user.getId(), user.getName());
            }
        }

        GetUsersResultDTO resultDTO = new GetUsersResultDTO(true, users, null);
        getClient().send(resultDTO);
    }
}