package com.hamming.storim.server;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.VerbDetailsDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.*;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.*;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.*;

import java.net.Socket;
import java.util.HashMap;

public class UserDataServerConnection extends ClientConnection {


    public UserDataServerConnection(String id, Socket s, ServerWorker serverWorker) {
        super(id, s, serverWorker);
    }

    @Override
    public void addActions() {
        //TODO what kind of messages do you receive from the userdataserver?
    }



    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {

    }



}
