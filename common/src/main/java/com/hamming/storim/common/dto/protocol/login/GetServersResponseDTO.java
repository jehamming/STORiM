package com.hamming.storim.common.dto.protocol.login;

import com.hamming.storim.common.dto.protocol.ProtocolASyncRequestDTO;

import java.util.List;

public class GetServersResponseDTO extends ProtocolASyncRequestDTO {

    private final List<ServerRegistrationDTO> servers;

    public GetServersResponseDTO(List<ServerRegistrationDTO> servers) {
        this.servers = servers;
    }

    public List<ServerRegistrationDTO> getServers() {
        return servers;
    }
}
