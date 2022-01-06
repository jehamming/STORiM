package com.hamming.storim.server.common;


import com.hamming.storim.common.Protocol;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.server.common.action.Action;

import java.util.HashMap;
import java.util.Map;

public class ProtocolHandler<T extends Action> implements Protocol {

    private Map<Class , T> actions;

    public ProtocolHandler() {
        actions = new HashMap<Class, T>();
    }

    public void addAction(Class clazz, T action) {
        // Users
        actions.put(clazz, action);
    }
    public void clearActions() {
        actions = new HashMap<>();
    }

    public T getAction(ProtocolDTO dto) {
        return actions.get(dto.getClass());
    }


}
