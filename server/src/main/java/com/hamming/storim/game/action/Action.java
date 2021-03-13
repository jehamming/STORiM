package com.hamming.storim.game.action;

import com.hamming.storim.model.dto.protocol.ProtocolDTO;

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
