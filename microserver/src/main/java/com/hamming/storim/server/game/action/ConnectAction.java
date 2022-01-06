package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.login.ConnectRequestDTO;
import com.hamming.storim.common.dto.protocol.login.ConnectResultDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.DTOFactory;
import com.hamming.storim.server.common.dto.protocol.loginserver.VerifyUserResponseDTO;
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
        // Verify UserId + token with LoginServer
        boolean validUserAndToken = client.verifyUser(getDto().getUserId(), getDto().getToken());
        UserDto userDto = null;
        LocationDto locationDto = null;
        String errorMessage = null;
        if ( validUserAndToken ) {
            // Send Location & Details
            client.sendGameState(client.getCurrentUser());
            client.sendRoom(client.getCurrentUser().getLocation().getRoom());
            client.sendThingsInRoom(client.getCurrentUser().getLocation().getRoom());
            client.sendUserLocation(client.getCurrentUser());
            userDto = DTOFactory.getInstance().getUserDTO(client.getCurrentUser());
            locationDto = DTOFactory.getInstance().getLocationDTO(client.getCurrentUser().getLocation());
        } else {
            errorMessage = "Not a valid user or valid token!";
        }


        ConnectResultDTO connectResultDTO = new ConnectResultDTO(validUserAndToken, errorMessage, userDto, locationDto);
        client.send(connectResultDTO);

    }

}
