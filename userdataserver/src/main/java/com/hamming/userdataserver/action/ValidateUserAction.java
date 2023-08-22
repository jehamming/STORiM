package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.ValidateUserRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.ValidateUserResponseDTO;
import com.hamming.userdataserver.DTOFactory;
import com.hamming.userdataserver.Session;
import com.hamming.userdataserver.UserDataClientConnection;
import com.hamming.userdataserver.factories.UserFactory;
import com.hamming.userdataserver.model.User;

public class ValidateUserAction extends Action<ValidateUserRequestDTO> {

    private ServerWorker serverWorker;

    public ValidateUserAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        UserDataClientConnection client = (UserDataClientConnection) getClient();
        String errorMessage = null;
        UserDto userDto = null;
        String token = null;
        boolean success = false;
        String username = getDto().getUsername();
        String password = getDto().getPassword();
        boolean admin = false;

        User user = UserFactory.getInstance().findUserByUsername(username);
        if (user != null ) {
            if ( user.getPassword().equals(password)) {
                userDto = DTOFactory.getInstance().getUserDTO(user);
                Long userId = user.getId();
                String source = getDto().getSource();
                Session session = client.getStorimUserDataServer().getSessionManager().createSession(userId, source);
                token = session.getToken();
                success = true;
                if ( client.getStorimUserDataServer().getAdmins().contains(user) ) {
                    admin = true;
                }
            } else {
                errorMessage = "User " + username +", password is incorrect!";
            }
        } else {
            errorMessage = "User " + username +" not found!";
        }
        ValidateUserResponseDTO validateUserResponseDTO = new ValidateUserResponseDTO(success, userDto, errorMessage, token, admin);
        getClient().send(validateUserResponseDTO);
    }
}