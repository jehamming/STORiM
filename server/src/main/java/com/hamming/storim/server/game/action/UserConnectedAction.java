package com.hamming.storim.server.game.action;

import com.hamming.storim.server.ClientConnection;
import com.hamming.storim.server.factories.DTOFactory;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.model.User;
import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.UserConnectedDTO;

public class UserConnectedAction extends Action {
    private GameController controller;
    private ClientConnection client;

    private User user;

    public UserConnectedAction(GameController controller, ClientConnection client, User user) {
        this.controller = controller;
        this.client = client;
        this.user = user;
    }

    @Override
    public void execute() {
        UserDto userDto = DTOFactory.getInstance().getUserDTO(user);
        client.sendRoom(user.getLocation().getRoom());
        LocationDto locationDto = DTOFactory.getInstance().getLocationDTO(user.getLocation());
        UserConnectedDTO connectedDTO = DTOFactory.getInstance().getUserConnectedDTO(userDto, locationDto);
        client.send(connectedDTO);
    }

}
