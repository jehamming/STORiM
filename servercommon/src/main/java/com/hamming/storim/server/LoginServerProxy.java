package com.hamming.storim.server;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.util.Logger;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.dto.protocol.loginserver.VerifyUserRequestDTO;
import com.hamming.storim.server.common.dto.protocol.loginserver.VerifyUserResponseDTO;

public class LoginServerProxy {
    private ClientConnection connection;

    public LoginServerProxy(ClientConnection connection) {
        this.connection = connection;
    }

    public void send(ProtocolDTO dto) {
        connection.send(dto);
    }

    public UserDto verifyUser(Long userId, String token) {
        UserDto verifiedUser = null;
        VerifyUserRequestDTO dto = new VerifyUserRequestDTO(userId, token);
        VerifyUserResponseDTO response = connection.sendReceive(dto, VerifyUserResponseDTO.class);
        if (response.isSuccess() ) {
            verifiedUser = response.getUser();
        } else {
            Logger.info(this, " Error :" + response.getErrorMessage());
        }
        return verifiedUser;
    }


}
