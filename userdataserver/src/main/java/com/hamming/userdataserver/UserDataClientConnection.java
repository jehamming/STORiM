package com.hamming.userdataserver;

import com.hamming.storim.common.util.Logger;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.userdataserver.action.*;

import java.net.Socket;

public class UserDataClientConnection extends ClientConnection {

    private STORIMUserDataServer storimUserDataServer;

    public UserDataClientConnection(STORIMUserDataServer srv, String id, Socket s, ServerWorker serverWorker) {
        super(id, s, serverWorker);
        this.storimUserDataServer = srv;
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
        getProtocolHandler().addAction(new VerifyUserTokenAction(getServerWorker(), this));
        getProtocolHandler().addAction(new GetUsersAction(getServerWorker(), this));
        getProtocolHandler().addAction(new AddUserAction(getServerWorker(), this));
        getProtocolHandler().addAction(new UpdateUserAction(getServerWorker(), this));
        getProtocolHandler().addAction(new DeleteUserAction(getServerWorker(), this));
        getProtocolHandler().addAction(new SearchUsersAction(getServerWorker(), this));
        getProtocolHandler().addAction(new GetUserByUsernameAction(getServerWorker(), this));
        getProtocolHandler().addAction(new SetServerDetailsAction(this));
    }


    @Override
    public void connected() {}

    public STORIMUserDataServer getStorimUserDataServer() {
        return storimUserDataServer;
    }

    @Override
    public void disconnected() {
        Logger.info(this, "disconnected");
    }
}
