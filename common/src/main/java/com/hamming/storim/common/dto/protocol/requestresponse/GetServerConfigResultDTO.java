package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.ServerConfigurationDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetServerConfigResultDTO extends ResponseDTO {

    private ServerConfigurationDTO serverConfigurationDTO;

    public GetServerConfigResultDTO(ServerConfigurationDTO config) {
        super(true, null);
        this.serverConfigurationDTO = config;
    }

    public ServerConfigurationDTO getServerConfigurationDTO() {
        return serverConfigurationDTO;
    }

    @Override
    public String toString() {
        return "GetServerConfigResultDTO{" +
                "serverConfigurationDTO=" + serverConfigurationDTO +
                '}';
    }
}
