package com.hamming.userdataserver;

import com.hamming.storim.common.dto.protocol.request.ClientTypeDTO;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.GetUserRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.UpdateUserRoomDto;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.GetVerbResponseDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.GetVerbsRequestDTO;
import com.hamming.userdataserver.action.GetUserAction;
import com.hamming.userdataserver.action.GetVerbAction;
import com.hamming.userdataserver.action.GetVerbsAction;
import com.hamming.userdataserver.action.UpdateUserRoomAction;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class UserDataClientConnection extends ClientConnection {


    public UserDataClientConnection(ClientTypeDTO clientTypeDTO, Socket s, ObjectInputStream in, ObjectOutputStream out, ServerWorker serverWorker) {
        super(clientTypeDTO, s, in, out, serverWorker);
    }

    @Override
    public void connectionClosed() {

    }

    @Override
    public void addActions() {
        getProtocolHandler().addAction(GetUserRequestDTO.class, new GetUserAction(getServerWorker(), this));
        getProtocolHandler().addAction(UpdateUserRoomDto.class, new UpdateUserRoomAction(getServerWorker(), this));
        getProtocolHandler().addAction(GetVerbsRequestDTO.class, new GetVerbsAction(getServerWorker(), this));
        getProtocolHandler().addAction(GetVerbResponseDTO.class, new GetVerbAction(getServerWorker(), this));
    }
}
