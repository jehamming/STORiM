package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.VerifyAdminRequestDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.VerifyAdminResponseDTO;
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

public class VerifyAdminPasswordAction extends Action<VerifyAdminRequestDTO> {

    private ServerWorker serverWorker;

    public VerifyAdminPasswordAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        UserDataClientConnection client = (UserDataClientConnection) getClient();
        boolean success = false;
        String errorMessage = null;
        String adminPassword = getDto().getAdminPassword();

        if ( client.getStorimUserDataServer().getAdminPassword().equals(adminPassword)) {
            success = true;
        } else {
            errorMessage = "Password is incorrect";
        }

        VerifyAdminResponseDTO responseDTO = new VerifyAdminResponseDTO(success, errorMessage);
        getClient().send(responseDTO);
    }

}
