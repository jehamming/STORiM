package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.ConnectRequestDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.ConnectResultDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.DTOFactory;
import com.hamming.storim.server.game.GameController;

public class ConnectAction extends Action<ConnectRequestDTO> {
    private GameController controller;
    private STORIMClientConnection client;


    public ConnectAction(GameController controller, STORIMClientConnection client) {
        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        boolean validUserAndToken = client.verifyUser(getDto().getUserId(), getDto().getToken());
        if ( validUserAndToken ) {
            Long roomId = getDto().getRoomId();
            client.setRoom(roomId);
            client.sendUsersInRoom();
        } else {
            String errorMessage = "Not a valid user or valid token!";
            client.send(new ConnectResultDTO(false, errorMessage, null, null));
        }
    }

}
