package com.hamming.storim.common.net;

import com.hamming.storim.common.dto.protocol.Protocol;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class ResponseContainer<T extends ResponseDTO> {
    public static final long RESPONSE_TIMEOUT = 4000; // 4 Seconds
    private T response;
    private Class<T> responseClass;

    public void setResponse(T response) {
        this.response = response;
    }

    public T getResponse() {
        return response;
    }

    public Class<T> getResponseClass() {
        return responseClass;
    }

    public void setResponseClass(Class<T> responseClass) {
        if ( Protocol.getInstance().getClass(responseClass.getSimpleName()) == null ) {
            // Need this for JSON2Java without forName() stuff
            Protocol.getInstance().registerClass(responseClass.getSimpleName(), responseClass);
        }
        this.responseClass = responseClass;
    }
}

