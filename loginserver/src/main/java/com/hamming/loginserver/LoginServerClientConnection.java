package com.hamming.loginserver;

import com.hamming.loginserver.action.AddServerAction;
import com.hamming.loginserver.action.GetRoomsForServerAction;
import com.hamming.loginserver.action.GetServersAction;
import com.hamming.loginserver.action.VerifyUserAction;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.dto.protocol.loginserver.AddServerRequestDTO;
import com.hamming.storim.server.common.dto.protocol.loginserver.VerifyUserRequestDTO;

import java.net.Socket;

public class LoginServerClientConnection extends ClientConnection {



    public LoginServerClientConnection(String id, Socket s, LoginServerWorker serverWorker) {
        super(id, s, serverWorker);
    }


    @Override
    public void addActions() {
        LoginServerWorker serverWorker = (LoginServerWorker) getServerWorker();
        getProtocolHandler().addAction(new AddServerAction(this, serverWorker));
        getProtocolHandler().addAction(new VerifyUserAction(serverWorker, this));
        getProtocolHandler().addAction(new GetServersAction(serverWorker, this));
        getProtocolHandler().addAction(new GetRoomsForServerAction(serverWorker, this));
    }


    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {
        LoginServerWorker loginServerWorker = (LoginServerWorker) getServerWorker();
        loginServerWorker.removeRegisteredServer(this);
    }
}
