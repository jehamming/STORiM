package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.GetUserDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetUserResultDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.DTOFactory;
import com.hamming.storim.server.common.model.User;
import com.hamming.storim.server.game.GameController;

public class GetUserAction extends Action<GetUserDTO> {
    private GameController controller;

    public GetUserAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        User user = controller.getGameState().findUserById(getDto().getUserID());
        if ( user != null ) {
            UserDto userDto = DTOFactory.getInstance().getUserDTO(user);
            GetUserResultDTO getUserResultDTO = DTOFactory.getInstance().getGetUserResultDTO(true, null, userDto);
            getClient().send(getUserResultDTO);
        } else {
            GetUserResultDTO getUserResultDTO = DTOFactory.getInstance().getGetUserResultDTO(true, "User not found!", null);
            getClient().send(getUserResultDTO);
        }
    }
}