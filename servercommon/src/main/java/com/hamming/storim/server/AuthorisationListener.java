package com.hamming.storim.server;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.server.common.model.BasicObject;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class AuthorisationListener<T extends BasicObject> {

    public Class getListenForObjectClass() {
        Class clazz = (Class<T>)
                ((ParameterizedType)getClass()
                        .getGenericSuperclass())
                        .getActualTypeArguments()[0];
        return clazz;
    }

    public abstract void authorisationChanged(T object, List<Long> old);
}
