package com.hamming.loginserver.action;

import com.hamming.loginserver.LoginServerWorker;
import com.hamming.loginserver.ServerRegistration;
import com.hamming.storim.common.dto.protocol.login.GetServersResponseDTO;
import com.hamming.storim.common.dto.protocol.login.ServerRegistrationDTO;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.common.dto.protocol.login.GetServersRequestDTO;

import java.util.ArrayList;
import java.util.List;

public class GetServersAction extends Action<GetServersRequestDTO> {

    private LoginServerWorker serverWorker;

    public GetServersAction(LoginServerWorker serverWorker) {
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

        GetServersResponseDTO getServersResponseDTO = new GetServersResponseDTO(servers);

        setResult(getServersResponseDTO);
    }
}