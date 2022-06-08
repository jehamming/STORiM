package com.hamming.loginserver.action;

import com.hamming.loginserver.LoginServerWorker;
import com.hamming.loginserver.Session;
import com.hamming.loginserver.UserClientConnection;
import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginResultDTO;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.GetLocationDto;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.GetLocationResponseDto;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.ValidateUserRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.ValidateUserResponseDTO;

public class LoginAction extends Action<LoginDTO> {

    private LoginServerWorker serverWorker;

    public LoginAction(LoginServerWorker serverWorker, UserClientConnection clientConnection) {
        super(clientConnection);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        boolean loginSucceeded = false;
        Session session = null;
        String token = null;
        LocationDto location = null;
        String errorMessage = null;
        UserDto user = null;
        String username = getDto().getUsername();
        String password = getDto().getPassword();
        // Verify User with UserDataServer
        ValidateUserRequestDTO validateUserRequestDTO = new ValidateUserRequestDTO(username, password);
        ValidateUserResponseDTO validateUserResponseDTO = serverWorker.getLoginServer().getUserDataServerConnection().sendReceive(validateUserRequestDTO, ValidateUserResponseDTO.class);
        if ( validateUserResponseDTO != null) {
            user = validateUserResponseDTO.getUser();
            if (user != null) {
                // Success!
                ((UserClientConnection) getClient()).setCurrentUser(user);
                // Create a new Session for this connection
                String source = getClient().getId();
                session = serverWorker.getLoginServer().getSessionManager().createSession(user.getId(), source);
                // Check location of the user
                GetLocationDto getLocationDto = new GetLocationDto(user.getId());
                GetLocationResponseDto getLocationResponseDto = serverWorker.getLoginServer().getUserDataServerConnection().sendReceive(getLocationDto, GetLocationResponseDto.class);
                location = getLocationResponseDto.getLocation();

                loginSucceeded = true;
            } else {
                errorMessage = validateUserResponseDTO.getErrorMessage();
            }
        } else {
            errorMessage = getClass().getSimpleName() + ":Something went wrong";
        }

        // Set Result (Sync)
        if ( loginSucceeded ) {
            token = session.getToken();

        }
        LoginResultDTO loginResultDTO = new LoginResultDTO(loginSucceeded, token, errorMessage, user, location);

        getClient().send(loginResultDTO);

    }

}
