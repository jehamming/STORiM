package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.requestresponse.GetUsersRequestDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetUsersResultDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.SearchUsersRequestDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.SearchUsersResultDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMException;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;

import java.util.HashMap;

public class SearchUsersAction extends Action<SearchUsersRequestDTO> {
    private GameController controller;


    public SearchUsersAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        String errorMessage = null;
        HashMap<Long, String> users = null;
        boolean success = false;
        try {
            users = client.getServer().getUserDataServerProxy().searchUsers(getDto().getSearchString());
            success = true;
        } catch (STORIMException e) {
            errorMessage = e.getMessage();
        }
        SearchUsersResultDTO resultDTO = new SearchUsersResultDTO(success, users, errorMessage);
        client.send(resultDTO);
    }

}
