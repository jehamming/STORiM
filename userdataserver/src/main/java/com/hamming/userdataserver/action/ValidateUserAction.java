package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.ValidateUserRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.ValidateUserResponseDTO;
import com.hamming.storim.server.common.dto.protocol.loginserver.VerifyUserResponseDTO;
import com.hamming.userdataserver.DTOFactory;
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
        String errorMessage = null;
        UserDto userDto = null;
        String username = getDto().getUsername();
        String password = getDto().getPassword();

        User user = UserFactory.getInstance().findUserByUsername(username);
        if (user != null ) {
            if ( user.getPassword().equals(password)) {
                userDto = DTOFactory.getInstance().getUserDTO(user);
            } else {
                errorMessage = "User " + username +", password is incorrect!";
            }
        } else {
            errorMessage = "User " + username +" not found!";
        }

        ValidateUserResponseDTO validateUserResponseDTO = new ValidateUserResponseDTO(userDto, errorMessage);
        getClient().send(validateUserResponseDTO);
    }
}