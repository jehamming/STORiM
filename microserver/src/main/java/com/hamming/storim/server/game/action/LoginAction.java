package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginResultDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMException;
import com.hamming.storim.server.ServerConfiguration;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.ValidateUserResponseDTO;
import com.hamming.storim.server.game.GameController;

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
        boolean serverAdmin = false;
        boolean userServerAdmin = false;
        boolean success = false;
        String error = "";

        try {
            // Verify User with UserDataServer
            ValidateUserResponseDTO responseDTO = client.getServer().getUserDataServerProxy().validateUser(client, username, password);
            success = responseDTO.isSuccess();
            if (responseDTO.isSuccess()) {
                client.setSessionToken(responseDTO.getSessionToken());
                userDto = responseDTO.getUser();
                ServerConfiguration serverConfiguration = client.getServer().getServerConfiguration();
                if ( serverConfiguration.getSuperAdmin() == userDto.getId() || serverConfiguration.getServerAdmins().contains(userDto.getId())) {
                    serverAdmin = true;
                    client.setServerAdmin(serverAdmin);
                }
                if ( responseDTO.isAdmin()) {
                    userServerAdmin = true;
                    client.setUserAdmin(userServerAdmin);
                }
                LoginResultDTO loginResultDTO = new LoginResultDTO(responseDTO.isSuccess(), client.getSessionToken(), responseDTO.getErrorMessage(), userDto, null, serverAdmin, userServerAdmin);
                getClient().send(loginResultDTO);
                // Send the details
                String token = responseDTO.getSessionToken();
                client.setSessionToken(token);

                client.currentUserConnected(userDto, roomId);
            } else {
                LoginResultDTO loginResultDTO = new LoginResultDTO(responseDTO.isSuccess(), client.getSessionToken(), responseDTO.getErrorMessage(), userDto, null, serverAdmin, userServerAdmin);
                getClient().send(loginResultDTO);
            }
        } catch (STORIMException e) {
            LoginResultDTO loginResultDTO = new LoginResultDTO(success, null, e.getMessage(), null, null, serverAdmin, userServerAdmin);
            getClient().send(loginResultDTO);
        }
    }

}
