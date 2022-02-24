package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.ServerRegistrationDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.util.List;

public class GetServerRegistrationsResponseDTO implements  ResponseDTO {

    private final List<ServerRegistrationDTO> servers;

    public GetServerRegistrationsResponseDTO(List<ServerRegistrationDTO> servers) {
        this.servers = servers;
    }

    public List<ServerRegistrationDTO> getServers() {
        return servers;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
