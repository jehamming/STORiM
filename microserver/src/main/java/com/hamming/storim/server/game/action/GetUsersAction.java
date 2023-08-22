package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.requestresponse.*;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMException;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;

import java.util.HashMap;

public class GetUsersAction extends Action<GetUsersRequestDTO> {
    private GameController controller;


    public GetUsersAction(GameController controller, STORIMClientConnection client) {
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
            if (client.isUserAdmin()) {
                users = client.getServer().getUserDataServerProxy().getAllUsers();
                success = true;
            } else {
                errorMessage = "No DataServer admin privileges for current user";
            }
        } catch (STORIMException e) {
            errorMessage = e.getMessage();
        }
        GetUsersResultDTO resultDTO = new GetUsersResultDTO(success, users, errorMessage);
        client.send(resultDTO);
    }

}
