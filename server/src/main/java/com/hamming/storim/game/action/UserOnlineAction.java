package com.hamming.storim.game.action;

import com.hamming.storim.ClientConnection;
import com.hamming.storim.factories.DTOFactory;
import com.hamming.storim.factories.UserFactory;
import com.hamming.storim.game.GameController;
import com.hamming.storim.model.Location;
import com.hamming.storim.model.User;
import com.hamming.storim.model.dto.LocationDto;
import com.hamming.storim.model.dto.UserDto;
import com.hamming.storim.model.dto.protocol.UserConnectedDTO;
import com.hamming.storim.model.dto.protocol.UserOnlineDTO;

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
