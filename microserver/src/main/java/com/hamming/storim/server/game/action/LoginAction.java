package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginResultDTO;
import com.hamming.storim.common.dto.protocol.serverpush.AvatarSetDTO;
import com.hamming.storim.common.dto.protocol.serverpush.SetCurrentUserDTO;
import com.hamming.storim.server.DTOFactory;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.SessionDto;
import com.hamming.storim.server.common.model.Location;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.game.ServerEvent;

public class LoginAction extends Action<LoginDTO> {
    private GameController controller;

    public LoginAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        boolean loginSucceeded = false;
        SessionDto session = null;
        LocationDto locationDto = null;
        String errorMessage = null;
        UserDto user = null;
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        String username = getDto().getUsername();
        String password = getDto().getPassword();
        Long roomId = getDto().getRoomID();
        // Verify User with UserDataServer

        user = client.getServer().getUserDataServerProxy().validateUser(client, username, password);

        if (user != null) {
            LoginResultDTO loginResultDTO = new LoginResultDTO(true, client.getSessionToken(), errorMessage, user, locationDto);
            getClient().send(loginResultDTO);
            // Send the details
            client.currentUserConnected(user, roomId);

        }


    }

}
