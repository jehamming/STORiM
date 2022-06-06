package com.hamming.userdataserver;

import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.*;
import com.hamming.storim.server.common.dto.protocol.dataserver.tile.*;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.GetUserRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.UpdateUserRoomDto;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.ValidateUserRequestDTO;
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
        getProtocolHandler().addAction(GetVerbDetailsRequestDTO.class, new GetVerbAction(getServerWorker(), this));
        getProtocolHandler().addAction(AddVerbRequestDto.class, new AddVerbAction(getServerWorker(), this));
        getProtocolHandler().addAction(DeleteVerbRequestDto.class, new DeleteVerbAction(getServerWorker(), this));
        getProtocolHandler().addAction(UpdateVerbRequestDto.class, new UpdateVerbAction(getServerWorker(), this));
        getProtocolHandler().addAction(GetAvatarsRequestDTO.class, new GetAvatarsAction(getServerWorker(), this));
        getProtocolHandler().addAction(AddAvatarRequestDto.class, new AddAvatarAction(getServerWorker(), this));
        getProtocolHandler().addAction(GetAvatarRequestDTO.class, new GetAvatarAction(getServerWorker(), this));
        getProtocolHandler().addAction(DeleteAvatarRequestDTO.class, new DeleteAvatarAction(getServerWorker(), this));
        getProtocolHandler().addAction(UpdateAvatarRequestDto.class, new UpdateAvatarAction(getServerWorker(), this));
        getProtocolHandler().addAction(SetAvatarRequestDto.class, new SetAvatarAction(getServerWorker(), this));
        getProtocolHandler().addAction(ValidateUserRequestDTO.class, new ValidateUserAction(getServerWorker(), this));
        getProtocolHandler().addAction(AddTileRequestDto.class, new AddTileAction(getServerWorker(), this));
        getProtocolHandler().addAction(GetTileRequestDTO.class, new GetTileAction(getServerWorker(), this));
        getProtocolHandler().addAction(GetTilesForUserRequestDTO.class, new GetTilesForUserAction(getServerWorker(), this));
        getProtocolHandler().addAction(AddThingRequestDto.class, new AddThingAction(getServerWorker(), this));
        getProtocolHandler().addAction(GetThingsForUserRequestDTO.class, new GetThingsForUserAction(getServerWorker(), this));
        getProtocolHandler().addAction(GetThingRequestDTO.class, new GetThingAction(getServerWorker(), this));
    }


    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {

    }
}
