package com.hamming.storim.server.common.action;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public abstract class Action<T extends ProtocolDTO> {
    private T dto;
    public ProtocolDTO result;

    public abstract void execute();

    public void setDTO(T dto) {
        this.dto = dto;
    }

    public T getDto() {
        return dto;
    }

    public ProtocolDTO getResult() {
        return result;
    }

    public void setResult(ProtocolDTO result) {
        synchronized (this) {
            this.result = result;
            this.notify();
        }
    }
}
