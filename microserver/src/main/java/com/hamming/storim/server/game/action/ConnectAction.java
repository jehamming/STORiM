package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.ConnectRequestDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.ConnectResultDTO;
import com.hamming.storim.common.dto.protocol.serverpush.SetCurrentUserDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.DTOFactory;
import com.hamming.storim.server.common.model.User;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.game.GameStateEvent;

public class ConnectAction extends Action<ConnectRequestDTO> {
    private GameController controller;


    public ConnectAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection)  getClient();
        boolean validUserAndToken = client.verifyUser(getDto().getUserId(), getDto().getToken());
        if ( validUserAndToken ) {
            Long roomId = getDto().getRoomId();
            client.sendGameState();
            client.setRoom(roomId);
            // Send current User info
            User currentUser = client.getCurrentUser();
            UserDto userDto = DTOFactory.getInstance().getUserDTO(currentUser);
            LocationDto locationDto = DTOFactory.getInstance().getLocationDTO(currentUser.getLocation());
            SetCurrentUserDTO setCurrentUserDTO = new SetCurrentUserDTO(userDto, locationDto);
            client.send(setCurrentUserDTO);
            // Notify the gamecontroller
            controller.addOnlineUser(client, client.getCurrentUser());
        } else {
            String errorMessage = "Not a valid user or valid token!";
            getClient().send(new ConnectResultDTO(false, errorMessage, null, null));
        }
    }

}
