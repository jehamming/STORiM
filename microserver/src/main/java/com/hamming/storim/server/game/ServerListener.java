package com.hamming.storim.server.game;

import com.hamming.storim.common.dto.UserDto;

public interface ServerListener {


    void serverEvent(ServerEvent event);

}
