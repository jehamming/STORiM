package com.hamming.storim.server.common.action;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.server.common.ClientConnection;

public abstract class Action<T extends ProtocolDTO> {
    private T dto;
    private ClientConnection client;

    public Action(ClientConnection client) {
        this.client = client;
    }

    public abstract void execute();

    public void setDTO(T dto) {
        this.dto = dto;
    }

    public T getDto() {
        return dto;
    }

    public ClientConnection getClient() {
        return client;
    }
}
