package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.serverpush.UserOnlineDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.dto.DTOFactory;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.model.User;
import com.hamming.storim.server.game.GameController;

public class UserOnlineAction extends Action {
    private GameController controller;
    private STORIMClientConnection client;
    private User user;

    public UserOnlineAction(GameController controller, STORIMClientConnection client, User user) {

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
