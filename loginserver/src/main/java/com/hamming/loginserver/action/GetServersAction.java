package com.hamming.loginserver.action;

import com.hamming.loginserver.LoginServerWorker;
import com.hamming.loginserver.ServerRegistration;
import com.hamming.loginserver.UserClientConnection;
import com.hamming.storim.common.dto.protocol.requestresponse.GetServerRegistrationsResponseDTO;
import com.hamming.storim.common.dto.ServerRegistrationDTO;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.common.dto.protocol.requestresponse.GetServerRegistrationsDTO;

import java.util.ArrayList;
import java.util.List;

public class GetServersAction extends Action<GetServerRegistrationsDTO> {

    private LoginServerWorker serverWorker;

    public GetServersAction(LoginServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
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

        getClient().send(getServersResponseDTO);
    }
}