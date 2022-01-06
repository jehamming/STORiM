package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.UserConnectedDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.DTOFactory;
import com.hamming.storim.server.common.model.User;
import com.hamming.storim.server.game.GameController;

public class UserConnectedAction extends Action {
    private GameController controller;
    private STORIMClientConnection client;

    private User user;

    public UserConnectedAction(GameController controller, STORIMClientConnection client, User user) {

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
