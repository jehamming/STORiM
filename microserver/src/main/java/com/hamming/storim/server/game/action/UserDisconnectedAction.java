package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.serverpush.UserDisconnectedDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.dto.DTOFactory;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.model.User;
import com.hamming.storim.server.game.GameController;

public class UserDisconnectedAction extends Action {
    private GameController controller;
    private STORIMClientConnection client;
    private User user;

    public UserDisconnectedAction(GameController controller, STORIMClientConnection client, User user) {

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
