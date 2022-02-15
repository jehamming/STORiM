package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.serverpush.UserConnectedDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.model.Location;
import com.hamming.storim.server.game.GameController;

public class UserConnectedAction extends Action {
    private GameController controller;

    private UserDto user;

    public UserConnectedAction(GameController controller, STORIMClientConnection client, UserDto user) {
        super(client);
        this.controller = controller;
        this.user = user;
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        UserConnectedDTO connectedDTO = new UserConnectedDTO(user.getId(), user.getName());
        getClient().send(connectedDTO);

        Location currentUserLocation = controller.getGameState().getLocation(client.getCurrentUser().getId());
        Location userLocation = controller.getGameState().getLocation(user.getId());
        if ( currentUserLocation.getRoom().getId().equals( userLocation.getRoom().getId())) {
            client.sendUserInRoom(user);
        }
    }

}
