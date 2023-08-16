package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetUserDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetUserResultDTO;
import com.hamming.storim.server.DTOFactory;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMException;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;

public class GetUserAction extends Action<GetUserDTO> {
    private GameController controller;

    public GetUserAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        boolean success = false;
        String errorMessage = null;
        UserDto user = null;
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        try {
            user = client.getServer().getUserDataServerProxy().getUser(getDto().getUserID());
            if (user != null) {
                success = true;
            } else {
                errorMessage = "Got not User from the server with id:" + getDto().getUserID();
            }
        } catch (STORIMException e) {
            errorMessage = e.getMessage();
        }

        GetUserResultDTO resultDTO = new GetUserResultDTO(success, user, errorMessage);

        getClient().send(resultDTO);
    }
}