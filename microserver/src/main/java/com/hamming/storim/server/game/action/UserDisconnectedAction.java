package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.serverpush.UserDisconnectedDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.dto.DTOFactory;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.model.User;
import com.hamming.storim.server.game.GameController;

public class UserDisconnectedAction extends Action {
    private GameController controller;
    private User user;

    public UserDisconnectedAction(GameController controller, STORIMClientConnection client, User user) {
        super(client);
        this.controller = controller;
        this.user = user;
    }

    @Override
    public void execute() {
        controller.userDisconnected(getClient(), user);
    }


}
