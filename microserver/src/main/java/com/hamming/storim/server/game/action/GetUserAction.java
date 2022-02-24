package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.GetUserDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetUserResultDTO;
import com.hamming.storim.server.DTOFactory;
import com.hamming.storim.server.STORIMClientConnection;
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
        UserDto user = controller.getGameState().findUserById(getDto().getUserID());
        GetUserResultDTO getUserResultDTO = new GetUserResultDTO(user);
        getClient().send(getUserResultDTO);
    }
}