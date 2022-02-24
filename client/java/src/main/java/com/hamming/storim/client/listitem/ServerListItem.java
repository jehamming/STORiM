package com.hamming.storim.client.listitem;

import com.hamming.storim.common.dto.ServerRegistrationDTO;

public class ServerListItem {
    private ServerRegistrationDTO serverRegistrationDTO;
    public ServerListItem(ServerRegistrationDTO serverRegistrationDTO) {
        this.serverRegistrationDTO = serverRegistrationDTO;
    }

    public ServerRegistrationDTO getServerRegistrationDTO() {
        return serverRegistrationDTO;
    }

    @Override
    public String toString() {
        return serverRegistrationDTO.getServerName();
    }
}
