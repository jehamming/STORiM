package com.hamming.storim.server.game.action;

import com.hamming.storim.server.ClientConnection;
import com.hamming.storim.server.factories.DTOFactory;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.model.User;
import com.hamming.storim.common.dto.protocol.UserDisconnectedDTO;

public class UserDisconnectedAction extends Action{
    private GameController controller;
    private ClientConnection client;
    private User user;

    public UserDisconnectedAction(GameController controller, ClientConnection client, User user) {
        this.controller = controller;
        this.client = client;
        this.user = user;
    }

    @Override
    public void execute() {
        UserDisconnectedDTO dto = DTOFactory.getInstance().getUserDisconnectedDTO(user.getId());
        controller.userDisconnected(user);
        client.send(dto);
    }


}
