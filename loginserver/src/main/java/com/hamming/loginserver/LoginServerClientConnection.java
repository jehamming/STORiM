package com.hamming.loginserver;

import com.hamming.loginserver.action.AddServerAction;
import com.hamming.loginserver.action.VerifyUserAction;
import com.hamming.storim.common.dto.protocol.ClientTypeDTO;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.dto.protocol.loginserver.AddServerRequestDTO;
import com.hamming.storim.server.common.dto.protocol.loginserver.VerifyUserRequestDTO;
import sun.rmi.runtime.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class LoginServerClientConnection extends ClientConnection {



    public LoginServerClientConnection(ClientTypeDTO clientTypeDTO, Socket s, ObjectInputStream in, ObjectOutputStream out, LoginServerWorker serverWorker) {
        super(clientTypeDTO, s, in, out, serverWorker);
    }

    @Override
    public void connectionClosed() {
        LoginServerWorker loginServerWorker = (LoginServerWorker) getServerWorker();
        loginServerWorker.removeRegisteredServer(getClientType().getName());
    }

    @Override
    public void addActions() {
        LoginServerWorker serverWorker = (LoginServerWorker) getServerWorker();
        getProtocolHandler().addAction(AddServerRequestDTO.class, new AddServerAction((serverWorker)));
        getProtocolHandler().addAction(VerifyUserRequestDTO.class, new VerifyUserAction(serverWorker, this));
    }


}
