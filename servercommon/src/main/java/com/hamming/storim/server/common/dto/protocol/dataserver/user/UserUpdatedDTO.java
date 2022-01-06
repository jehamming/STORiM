package com.hamming.storim.server.common.dto.protocol.dataserver.user;

import com.hamming.storim.common.dto.protocol.ProtocolResponseDTO;
import com.hamming.storim.server.common.model.User;

public class UserUpdatedDTO extends ProtocolResponseDTO {

    private User user;

    public UserUpdatedDTO( User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "UserUpdatedDTO{" +
                "user=" + user +
                '}';
    }
}
                                                                                                                                          