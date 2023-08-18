package com.hamming.storim.server;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.server.common.model.BasicObject;

import java.util.List;

public interface AuthorisationListener<T extends BasicObject> {

    public void authorisationChanged(T object, List<Long> old);
}
