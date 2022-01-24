package com.hamming.loginserver.action;

import com.hamming.loginserver.LoginServerClientConnection;
import com.hamming.loginserver.LoginServerWorker;
import com.hamming.loginserver.ServerRegistration;
import com.hamming.loginserver.UserClientConnection;
import com.hamming.storim.common.dto.protocol.requestresponse.GetServerRegistrationsResponseDTO;
import com.hamming.storim.common.dto.ServerRegistrationDTO;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.common.dto.protocol.requestresponse.GetServerRegistrationsRequestDTO;

import java.util.ArrayList;
import java.util.List;

public class GetServersAction extends Action<GetServerRegistrationsRequestDTO> {

    private LoginServerWorker serverWorker;
    private UserClientConnection client;

    public GetServersAction(LoginServerWorker serverWorker, UserClientConnection client) {
        this.serverWorker = serverWorker;
        this.client = client;
    }

    @Override
    public void execute() {
        List<ServerRegistration> serverRegistrations = serverWorker.getRegisteredServers();
        List<ServerRegistrationDTO> servers = new ArrayList<>();

        for (ServerRegistration registration : serverRegistrations ) {
            ServerRegistrationDTO serverRegistrationDTO = new ServerRegistrationDTO(registration.getServerName(), registration.getServerURL(), registration.getServerPort());
            servers.add(serverRegistrationDTO);
        }

        GetServerRegistrationsResponseDTO getServersResponseDTO = new GetServerRegistrationsResponseDTO(servers);

        client.send(getServersResponseDTO);
    }
}