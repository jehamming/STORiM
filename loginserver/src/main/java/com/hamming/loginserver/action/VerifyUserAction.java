package com.hamming.loginserver.action;

import com.hamming.loginserver.LoginServerClientConnection;
import com.hamming.loginserver.LoginServerWorker;
import com.hamming.storim.server.common.Session;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.GetUserRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.GetUserResultDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.VerifyUserTokenRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.VerifyUserTokenResponseDTO;

public class VerifyUserAction extends Action<VerifyUserTokenRequestDTO> {

    private LoginServerWorker serverWorker;

    public VerifyUserAction(LoginServerWorker serverWorker, LoginServerClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        UserDto userDto = null;
        boolean success = false;
        String errorMessage = null;
        Long userId = getDto().getUserId();
        String token = getDto().getToken();

        Session session = serverWorker.getLoginServer().getSessionManager().getSession(userId);
        if ( session != null && session.getToken().equals(token)) {
            // GetUser
            GetUserRequestDTO getUserRequestDTO = new GetUserRequestDTO(userId);
            GetUserResultDTO getUserResultDTO = serverWorker.getLoginServer().getUserDataServerConnection().sendReceive(getUserRequestDTO, GetUserResultDTO.class);
            if ( getUserResultDTO != null && getUserResultDTO.isSuccess() ) {
                success = true;
                userDto = getUserResultDTO.getUser();
            } else {
                errorMessage = "User not found: " + userId;
            }
        } else {
            if (session == null) {
                errorMessage = "No session not found for user " + userId;
            } else if (session != null && !session.getToken().equals(token)) {
                errorMessage = "Invalid session token for user " + userId;
            }
        }

        VerifyUserTokenResponseDTO responseDTO = new VerifyUserTokenResponseDTO(success, errorMessage, userDto );
        getClient().send(responseDTO);

    }

}
