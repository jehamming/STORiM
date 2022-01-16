package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.user.GetUserDTO;
import com.hamming.storim.common.dto.protocol.user.GetUserResultDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.UserCache;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.DTOFactory;
import com.hamming.storim.server.common.model.User;
import com.hamming.storim.server.game.GameController;

public class GetUserAction extends Action<GetUserDTO> {
    private GameController controller;
    private STORIMClientConnection client;

    public GetUserAction(GameController controller, STORIMClientConnection client) {

        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        User user = UserCache.getInstance().findUserById(getDto().getUserID());
        if ( user != null ) {
            UserDto userDto = DTOFactory.getInstance().getUserDTO(user);
            GetUserResultDTO getUserResultDTO = DTOFactory.getInstance().getGetUserResultDTO(true, null, userDto);
            client.send(getUserResultDTO);
        } else {
            GetUserResultDTO getUserResultDTO = DTOFactory.getInstance().getGetUserResultDTO(true, "User not found!", null);
            client.send(getUserResultDTO);
        }
    }
}