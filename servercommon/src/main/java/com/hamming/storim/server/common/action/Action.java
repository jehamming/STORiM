package com.hamming.storim.server.common.action;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.server.common.ClientConnection;

import java.lang.reflect.ParameterizedType;

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

    public Class getProtocolClass() {
        Class clazz = (Class<T>)
                ((ParameterizedType)getClass()
                        .getGenericSuperclass())
                        .getActualTypeArguments()[0];
        return clazz;
    }
}
