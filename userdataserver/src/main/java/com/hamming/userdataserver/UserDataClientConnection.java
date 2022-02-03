package com.hamming.userdataserver;

import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.GetUserRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.UpdateUserRoomDto;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.*;
import com.hamming.userdataserver.action.*;

import java.net.Socket;

public class UserDataClientConnection extends ClientConnection {


    public UserDataClientConnection(String id, Socket s, ServerWorker serverWorker) {
        super(id, s, serverWorker);
    }

    @Override
    public void addActions() {
        getProtocolHandler().addAction(GetUserRequestDTO.class, new GetUserAction(getServerWorker(), this));
        getProtocolHandler().addAction(UpdateUserRoomDto.class, new UpdateUserRoomAction(getServerWorker(), this));
        getProtocolHandler().addAction(GetVerbsRequestDTO.class, new GetVerbsAction(getServerWorker(), this));
        getProtocolHandler().addAction(GetVerbRequestDTO.class, new GetVerbAction(getServerWorker(), this));
        getProtocolHandler().addAction(AddVerbRequestDto.class, new AddVerbAction(getServerWorker(), this));
        getProtocolHandler().addAction(DeleteVerbRequestDto.class, new DeleteVerbAction(getServerWorker(), this));
        getProtocolHandler().addAction(UpdateVerbRequestDto.class, new UpdateVerbAction(getServerWorker(), this));
    }

    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {

    }
}
