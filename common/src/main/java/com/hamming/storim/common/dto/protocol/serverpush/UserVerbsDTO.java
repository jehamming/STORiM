package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

import java.util.HashMap;

public class UserVerbsDTO implements ProtocolDTO {

    private HashMap<Long, String> verbs;

    public UserVerbsDTO(HashMap<Long, String> verbs) {
        this.verbs = verbs;
    }

    public HashMap<Long, String> getVerbs() {
        return verbs;
    }

    @Override
    public String toString() {
        return "UserVerbsDTO{" +
                "verbs=" + verbs +
                '}';
    }
}
