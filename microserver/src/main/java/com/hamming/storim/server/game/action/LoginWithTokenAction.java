package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginWithTokenDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginWithTokenResultDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
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
        UserDto verifiedUser = client.verifyUserToken(getDto().getUserId(), getDto().getToken());
        if ( verifiedUser != null ) {
            client.setSessionToken(getDto().getToken());
            client.currentUserConnected(verifiedUser, getDto().getRoomId());
            connectSucceeded = true;
         } else {
            errorMessage = "Not a valid user or valid token!";
        }
        getClient().send(new LoginWithTokenResultDTO(connectSucceeded, errorMessage));
    }


}
