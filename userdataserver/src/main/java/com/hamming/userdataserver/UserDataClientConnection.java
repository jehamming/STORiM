package com.hamming.userdataserver;

import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.userdataserver.action.*;

import java.net.Socket;

public class UserDataClientConnection extends ClientConnection {


    public UserDataClientConnection(String id, Socket s, ServerWorker serverWorker) {
        super(id, s, serverWorker);
    }

    @Override
    public void addActions() {
        getProtocolHandler().addAction(new GetUserAction(getServerWorker(), this));
        getProtocolHandler().addAction(new GetVerbsAction(getServerWorker(), this));
        getProtocolHandler().addAction(new GetVerbAction(getServerWorker(), this));
        getProtocolHandler().addAction(new AddVerbAction(getServerWorker(), this));
        getProtocolHandler().addAction(new DeleteVerbAction(getServerWorker(), this));
        getProtocolHandler().addAction(new UpdateVerbAction(getServerWorker(), this));
        getProtocolHandler().addAction(new GetAvatarsAction(getServerWorker(), this));
        getProtocolHandler().addAction(new AddAvatarAction(getServerWorker(), this));
        getProtocolHandler().addAction(new GetAvatarAction(getServerWorker(), this));
        getProtocolHandler().addAction(new DeleteAvatarAction(getServerWorker(), this));
        getProtocolHandler().addAction(new UpdateAvatarAction(getServerWorker(), this));
        getProtocolHandler().addAction(new SetAvatarAction(getServerWorker(), this));
        getProtocolHandler().addAction(new ValidateUserAction(getServerWorker(), this));
        getProtocolHandler().addAction(new AddTileAction(getServerWorker(), this));
        getProtocolHandler().addAction(new GetTileAction(getServerWorker(), this));
        getProtocolHandler().addAction(new GetTilesForUserAction(getServerWorker(), this));
        getProtocolHandler().addAction(new AddThingAction(getServerWorker(), this));
        getProtocolHandler().addAction(new GetThingsForUserAction(getServerWorker(), this));
        getProtocolHandler().addAction(new GetThingAction(getServerWorker(), this));
        getProtocolHandler().addAction(new DeleteThingAction(getServerWorker(), this));
        getProtocolHandler().addAction(new UpdateThingAction(getServerWorker(), this));
        getProtocolHandler().addAction(new SetLocationAction(getServerWorker(), this));
        getProtocolHandler().addAction(new GetLocationAction(getServerWorker(), this));
    }


    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {

    }
}
