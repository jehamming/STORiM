package com.hamming.storim.game.action;

import com.hamming.storim.ClientConnection;
import com.hamming.storim.factories.DTOFactory;
import com.hamming.storim.factories.UserFactory;
import com.hamming.storim.game.GameController;
import com.hamming.storim.model.User;
import com.hamming.storim.model.dto.protocol.user.GetUserDTO;
import com.hamming.storim.model.dto.UserDto;
import com.hamming.storim.model.dto.protocol.user.GetUserResultDTO;

public class GetUserAction extends Action<GetUserDTO> {
    private GameController controller;
    private ClientConnection client;

    public GetUserAction(GameController controller, ClientConnection client) {
        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        User user = UserFactory.getInstance().findUserById(getDto().getUserID());
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