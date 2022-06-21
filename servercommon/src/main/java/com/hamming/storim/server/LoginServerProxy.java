package com.hamming.storim.server;

import com.hamming.storim.common.dto.ServerRegistrationDTO;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetRoomsForServerDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetRoomsForServerResponseDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetServerRegistrationsDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetServerRegistrationsResponseDTO;
import com.hamming.storim.common.util.Logger;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.dto.protocol.loginserver.VerifyUserRequestDTO;
import com.hamming.storim.server.common.dto.protocol.loginserver.VerifyUserResponseDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public List<ServerRegistrationDTO> getRegisteredServers() {
        List<ServerRegistrationDTO> servers =  new ArrayList<>();
        GetServerRegistrationsResponseDTO getServerRegistrationsResponseDTO = connection.sendReceive(new GetServerRegistrationsDTO(), GetServerRegistrationsResponseDTO.class);
        if (getServerRegistrationsResponseDTO != null) {
            servers = getServerRegistrationsResponseDTO.getServers();
        }
        return servers;
    }

    public HashMap<Long, String> getRoomsForServer(String serverName) {
        HashMap<Long, String> rooms = new HashMap<>();
        GetRoomsForServerDTO getRoomsForServerDTO = new GetRoomsForServerDTO(serverName);
        GetRoomsForServerResponseDTO getRoomsForServerResponseDTO = connection.sendReceive(getRoomsForServerDTO, GetRoomsForServerResponseDTO.class);
        if ( getRoomsForServerResponseDTO != null && getRoomsForServerResponseDTO.getRooms() != null ) {
            rooms = getRoomsForServerResponseDTO.getRooms();
        }
        return rooms;

    }
}
