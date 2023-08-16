package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginResultDTO;
import com.hamming.storim.common.dto.protocol.serverpush.AvatarSetDTO;
import com.hamming.storim.common.dto.protocol.serverpush.SetCurrentUserDTO;
import com.hamming.storim.server.DTOFactory;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMException;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.SessionDto;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.ValidateUserResponseDTO;
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
        UserDto userDto = null;
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        client.setSessionToken(null);
        String username = getDto().getUsername();
        String password = getDto().getPassword();
        Long roomId = getDto().getRoomID();

        try {
            // Verify User with UserDataServer
            ValidateUserResponseDTO responseDTO = client.getServer().getUserDataServerProxy().validateUser(client, username, password);

            if (responseDTO.isSuccess()) {
                client.setSessionToken(responseDTO.getSessionToken());
                userDto = responseDTO.getUser();
                LoginResultDTO loginResultDTO = new LoginResultDTO(responseDTO.isSuccess(), client.getSessionToken(), responseDTO.getErrorMessage(), userDto, null);
                getClient().send(loginResultDTO);
                // Send the details
                String token = responseDTO.getSessionToken();
                client.setSessionToken(token);

                client.currentUserConnected(userDto, roomId);
            } else {
                LoginResultDTO loginResultDTO = new LoginResultDTO(responseDTO.isSuccess(), client.getSessionToken(), responseDTO.getErrorMessage(), userDto, null);
                getClient().send(loginResultDTO);
            }

        } catch (STORIMException e) {
            ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), e.getMessage());
            client.send(errorDTO);
        }


    }

}
