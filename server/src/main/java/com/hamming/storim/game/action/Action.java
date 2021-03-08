package com.hamming.storim.game.action;

import com.hamming.storim.model.dto.DTO;

public abstract class Action<T extends DTO> {
    private T dto;

    public abstract void execute();

    public void setDTO(T dto) {
        this.dto = dto;
    }

    public T getDto() {
        return dto;
    }
}
