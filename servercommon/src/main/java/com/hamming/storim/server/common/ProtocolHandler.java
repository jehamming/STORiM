package com.hamming.storim.server.common;


import com.hamming.storim.common.dto.protocol.Protocol;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.util.Logger;
import com.hamming.storim.server.common.action.Action;

import java.util.HashMap;
import java.util.Map;

public class ProtocolHandler<T extends Action>  {

    private Map<Class , T> actions;

    public ProtocolHandler() {
        actions = new HashMap<Class, T>();
    }

    public void addAction(T action) {
        actions.put(action.getProtocolClass(), action);
        String name = action.getProtocolClass().getSimpleName();
        Protocol.getInstance().registerClass(name, action.getProtocolClass());
    }
    public void clearActions() {
        actions = new HashMap<>();
    }

    public T getAction(ProtocolDTO dto) {
        return actions.get(dto.getClass());
    }


}
