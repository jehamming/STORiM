package com.hamming.storim.common.dto.protocol;

import java.io.Serializable;

public abstract class ProtocolDTO implements Serializable {

    public final static int SYNC = 0;
    public final static int ASYNC = 1;

    private int type = ASYNC;

    public ProtocolDTO(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
