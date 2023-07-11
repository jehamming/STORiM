package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.ConnectDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.ConnectResultDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetAvatarResponseDTO;
import com.hamming.storim.common.dto.protocol.serverpush.AvatarSetDTO;
import com.hamming.storim.common.dto.protocol.serverpush.SetCurrentUserDTO;
import com.hamming.storim.server.DTOFactory;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.model.Location;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.game.ServerEvent;

public class ConnectAction extends Action<ConnectDTO> {
    private GameController controller;


    public ConnectAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection)  getClient();
        UserDto verifiedUser = client.verifyUserToken(getDto().getUserId(), getDto().getToken());
        if ( verifiedUser != null ) {
            client.setSessionToken(getDto().getToken());
            client.currentUserConnected(verifiedUser);
         } else {
            String errorMessage = "Not a valid user or valid token!";
            getClient().send(new ConnectResultDTO(false, errorMessage, null, null));
        }
    }


}
