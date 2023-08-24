package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.VerifyUserTokenRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.VerifyUserTokenResponseDTO;
import com.hamming.userdataserver.DTOFactory;
import com.hamming.userdataserver.Session;
import com.hamming.userdataserver.UserDataClientConnection;
import com.hamming.userdataserver.factories.UserFactory;
import com.hamming.userdataserver.model.User;

public class VerifyUserTokenAction extends Action<VerifyUserTokenRequestDTO> {

    private ServerWorker serverWorker;

    public VerifyUserTokenAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        UserDataClientConnection client = (UserDataClientConnection) getClient();
        UserDto userDto = null;
        boolean success = false;
        boolean admin = false;
        String errorMessage = null;
        Long userId = getDto().getUserId();
        String token = getDto().getToken();

        User user = UserFactory.getInstance().findUserById(userId);
        if (user == null) {
            errorMessage = "UserId '" + userId + "' is not a valid user";
        } else {
            Session session = client.getStorimUserDataServer().getSessionManager().getSession(userId);
            if (session != null && session.getToken().equals(token)) {
                success = true;
                userDto = DTOFactory.getInstance().getUserDTO(user);
                if ( client.getStorimUserDataServer().getAdmins().contains(user) ) {
                    admin = true;
                }
            } else {
                errorMessage = "No valid session found for UserId '" + userId + "' token '" +  session.getToken() + "'";
            }

        }

        VerifyUserTokenResponseDTO responseDTO = new VerifyUserTokenResponseDTO(success, errorMessage, userDto, admin);
        getClient().send(responseDTO);

    }

}
