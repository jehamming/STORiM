package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.serverpush.UserConnectedDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.model.User;
import com.hamming.storim.server.game.GameController;

public class UserConnectedAction extends Action {
    private GameController controller;

    private User user;

    public UserConnectedAction(GameController controller, STORIMClientConnection client, User user) {
        super(client);
        this.controller = controller;
        this.user = user;
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        UserConnectedDTO connectedDTO = new UserConnectedDTO(user.getId(), user.getName());
        getClient().send(connectedDTO);
        if ( client.getCurrentUser().getLocation().getRoom().getId().equals( user.getLocation().getRoom().getId())) {
            client.sendUserInRoom(user);
        }
    }

}
