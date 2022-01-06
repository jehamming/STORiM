package com.hamming.loginserver.action;

import com.hamming.loginserver.LoginServerClientConnection;
import com.hamming.loginserver.LoginServerWorker;
import com.hamming.loginserver.Session;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.GetUserRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.GetUserResultDTO;
import com.hamming.storim.server.common.dto.protocol.loginserver.VerifyUserRequestDTO;
import com.hamming.storim.server.common.dto.protocol.loginserver.VerifyUserResponseDTO;

public class VerifyUserAction extends Action<VerifyUserRequestDTO> {

    private LoginServerWorker serverWorker;
    private LoginServerClientConnection clientConnection;

    public VerifyUserAction(LoginServerWorker serverWorker, LoginServerClientConnection clientConnection) {
        this.serverWorker = serverWorker;
        this.clientConnection = clientConnection;
    }

    @Override
    public void execute() {
        UserDto userDto = null;
        String errorMessage = null;
        Long userId = getDto().getUserId();
        String token = getDto().getToken();

        Session session = serverWorker.getLoginServer().getSessionManager().getSession(userId);
        if ( session != null && session.getToken().equals(token)) {
            // GetUser
            UserDto user = new UserDto();
            user.setId(userId);
            GetUserRequestDTO getUserRequestDTO = new GetUserRequestDTO(user);
            GetUserResultDTO getUserResultDTO = (GetUserResultDTO) serverWorker.getLoginServer().getUserDataServerConnection().serverRequest(getUserRequestDTO);
            if ( getUserResultDTO != null && getUserResultDTO.isSuccess() ) {
                userDto = getUserResultDTO.getUser();
            }
        } else {
            if (session == null) {
                errorMessage = "No session not found for user " + userId;
            } else if (session != null && !session.getToken().equals(token)) {
                errorMessage = "Invalid session token for user " + userId;
            }
        }

        VerifyUserResponseDTO responseDTO = new VerifyUserResponseDTO(userDto, errorMessage);
        setResult(responseDTO);

    }

}
