package com.hamming.loginserver.action;

import com.hamming.loginserver.LoginServerWorker;
import com.hamming.loginserver.Session;
import com.hamming.loginserver.UserClientConnection;
import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginRequestDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginResultDTO;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.GetUserRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.GetUserResultDTO;

public class LoginAction extends Action<LoginRequestDTO> {

    private LoginServerWorker serverWorker;
    private UserClientConnection clientConnection;

    public LoginAction(LoginServerWorker serverWorker, UserClientConnection clientConnection) {
        this.serverWorker = serverWorker;
        this.clientConnection = clientConnection;
    }

    @Override
    public void execute() {
        boolean loginSucceeded = false;
        Session session = null;
        String token = null;
        LocationDto location = null;
        String errorMessage = null;
        String username = getDto().getUsername();
        String password = getDto().getPassword();
        UserDto user = new UserDto();
        user.setUsername(username);
        // Verify User with UserDataServer
        GetUserRequestDTO getUserRequestDTO = new GetUserRequestDTO(user);
        GetUserResultDTO getUserResultDTO = (GetUserResultDTO) serverWorker.getLoginServer().getUserDataServerConnection().sendReceive(getUserRequestDTO);
        if ( getUserResultDTO != null && getUserResultDTO.isSuccess() ) {
            user = getUserResultDTO.getUser();
            if ( user.getPassword().equals(password)) {
                // Success!
                clientConnection.setCurrentUser(user);
                // Create a new Session for this connection
                String source = clientConnection.getSource();
                session = serverWorker.getLoginServer().getSessionManager().createSession(user.getId(), source);
                // Check location of the user
                loginSucceeded = true;
            } else {
                errorMessage = "Password is incorrect";
            }
        } else {
            errorMessage = " User " + getDto().getUsername() + " not found!";
        }

        // Set Result (Sync)
        if ( loginSucceeded ) {
            token = session.getToken();

        }
        LoginResultDTO loginResultDTO = new LoginResultDTO(loginSucceeded, token, errorMessage, user, location);

        setResult(loginResultDTO);

    }

}
