package com.hamming.storim.server.game.action;

import com.hamming.storim.server.ClientConnection;
import com.hamming.storim.server.factories.DTOFactory;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.model.User;
import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.UserOnlineDTO;

public class UserOnlineAction extends Action {
    private GameController controller;
    private ClientConnection client;
    private User user;

    public UserOnlineAction(GameController controller, ClientConnection client, User user) {
        this.controller = controller;
        this.client = client;
        this.user = user;
    }

    @Override
    public void execute() {
        UserDto userDTO = DTOFactory.getInstance().getUserDTO(user);
        LocationDto locationDto = DTOFactory.getInstance().getLocationDTO(user.getLocation());
        UserOnlineDTO userOnlineDTO  = DTOFactory.getInstance().getUserOnlineDTO(userDTO, locationDto);
        client.send(userOnlineDTO);
    }

}
