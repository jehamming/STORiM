package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.ServerRegistrationDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetServerRegistrationsDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetServerRegistrationsResponseDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;

import java.util.List;

public class GetServersAction extends Action<GetServerRegistrationsDTO> {


    public GetServersAction(ClientConnection client) {
        super(client);
    }


    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        List<ServerRegistrationDTO> servers = client.getServer().getLoginServerProxy().getRegisteredServers();

        GetServerRegistrationsResponseDTO getServersResponseDTO = new GetServerRegistrationsResponseDTO(servers);

        getClient().send(getServersResponseDTO);
    }
}