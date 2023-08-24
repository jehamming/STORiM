package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginWithTokenDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginWithTokenResultDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMException;
import com.hamming.storim.server.ServerConfiguration;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.VerifyUserTokenResponseDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.ValidateUserResponseDTO;
import com.hamming.storim.server.game.GameController;

public class LoginWithTokenAction extends Action<LoginWithTokenDTO> {
    private GameController controller;


    public LoginWithTokenAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        boolean connectSucceeded = false;
        String errorMessage = null;
        STORIMClientConnection client = (STORIMClientConnection)  getClient();
        boolean serverAdmin = false;
        boolean userServerAdmin = false;
        // Verify User with UserDataServer
        VerifyUserTokenResponseDTO response = client.getServer().getUserDataServerProxy().verifyUserToken(client.getId(), getDto().getUserId(), getDto().getToken());

        if ( response != null && response.isSuccess() ) {
            UserDto userDto = response.getUser();
            ServerConfiguration serverConfiguration = client.getServer().getServerConfiguration();
            if ( serverConfiguration.getSuperAdmin() == userDto.getId() || serverConfiguration.getServerAdmins().contains(userDto.getId())) {
                serverAdmin = true;
                client.setServerAdmin(serverAdmin);
            }
            if ( response.isAdmin()) {
                userServerAdmin = true;
                client.setUserAdmin(userServerAdmin);
            }
            connectSucceeded = true;
            getClient().send(new LoginWithTokenResultDTO(connectSucceeded, errorMessage, serverAdmin, userServerAdmin));
            client.setSessionToken(getDto().getToken());
            client.currentUserConnected(userDto, getDto().getRoomId());
         } else {
            errorMessage = "Not a valid user or valid token!";
            getClient().send(new LoginWithTokenResultDTO(connectSucceeded, errorMessage, serverAdmin, userServerAdmin));
        }
    }


}
