package com.hamming.storim.game.action;

import com.hamming.storim.ClientConnection;
import com.hamming.storim.factories.DTOFactory;
import com.hamming.storim.factories.UserFactory;
import com.hamming.storim.game.GameController;
import com.hamming.storim.game.Protocol;
import com.hamming.storim.model.User;
import com.hamming.storim.model.Location;
import com.hamming.storim.model.dto.LocationDto;
import com.hamming.storim.model.dto.RoomDto;
import com.hamming.storim.model.dto.UserDto;
import com.hamming.storim.model.dto.protocol.UserConnectedDTO;

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
