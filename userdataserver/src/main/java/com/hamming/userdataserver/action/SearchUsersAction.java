package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.protocol.requestresponse.GetUsersRequestDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetUsersResultDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.SearchUsersRequestDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.SearchUsersResultDTO;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.userdataserver.factories.UserFactory;
import com.hamming.userdataserver.model.User;

import java.util.HashMap;

public class SearchUsersAction extends Action<SearchUsersRequestDTO> {

    private ServerWorker serverWorker;

    public SearchUsersAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        HashMap<Long, String> users = new HashMap<>();
        for (User user: UserFactory.getInstance().findUsers(getDto().getSearchString())) {
            if ( !user.equals( UserFactory.getInstance().getRootUser())) {
                users.put(user.getId(), user.getName());
            }
        }
        SearchUsersResultDTO resultDTO = new SearchUsersResultDTO(true, users, null);
        getClient().send(resultDTO);
    }
}