package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.game.ServerEvent;

public class UserDisconnectedAction extends Action {
    private GameController controller;
    private UserDto user;

    public UserDisconnectedAction(GameController controller, STORIMClientConnection client, UserDto user) {
        super(client);
        this.controller = controller;
        this.user = user;
    }

    @Override
    public void execute() {
        boolean contained = controller.getGameState().getOnlineUsers().remove(user);
        if (contained) {
            controller.fireServerEvent(getClient(), new ServerEvent(ServerEvent.Type.USERDISCONNECTED, user));
        }
    }


}
