package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public abstract class Action<T extends ProtocolDTO> {
    private T dto;

    public abstract void execute();

    public void setDTO(T dto) {
        this.dto = dto;
    }

    public T getDto() {
        return dto;
    }
}
