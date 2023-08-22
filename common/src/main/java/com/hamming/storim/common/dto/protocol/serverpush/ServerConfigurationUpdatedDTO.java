package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.ServerConfigurationDTO;
import com.hamming.storim.common.dto.VerbDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class ServerConfigurationUpdatedDTO extends ResponseDTO {

    private ServerConfigurationDTO serverConfigurationDTO;

    public ServerConfigurationUpdatedDTO(ServerConfigurationDTO serverConfigurationDTO) {
        super(true, null);
        this.serverConfigurationDTO = serverConfigurationDTO;
    }

    public ServerConfigurationDTO getServerConfigurationDTO() {
        return serverConfigurationDTO;
    }

    @Override
    public String toString() {
        return "ServerConfigurationUpdatedDTO{" +
                "serverConfigurationDTO=" + serverConfigurationDTO +
                '}';
    }
}
                                                                                                                                          