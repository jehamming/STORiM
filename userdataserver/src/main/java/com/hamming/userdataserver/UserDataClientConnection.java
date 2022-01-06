package com.hamming.userdataserver;

import com.hamming.storim.common.dto.protocol.ClientTypeDTO;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.GetUserRequestDTO;
import com.hamming.userdataserver.action.GetUserAction;

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
        // TODO Meer Actions
        getProtocolHandler().addAction(GetUserRequestDTO.class, new GetUserAction(getServerWorker(), this));
    }
}
