package com.hamming.storim.server.common.dto.protocol.dataserver.verb;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.util.HashMap;

public class GetVerbsResponseDTO implements ResponseDTO {

    private HashMap<Long, String> verbs;

    public GetVerbsResponseDTO(HashMap<Long, String> verbs) {
        this.verbs = verbs;
    }

    public HashMap<Long, String> getVerbs() {
        return verbs;
    }

    @Override
    public String toString() {
        return "GetVerbsResponseDTO{" +
                "verbs=" + verbs +
                '}';
    }
}
