package com.hamming.userdataserver;

import com.hamming.storim.common.dto.protocol.requestresponse.GetAvatarRequestDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetAvatarsRequestDTO;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.AddAvatarRequestDto;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.DeleteAvatarRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.UpdateAvatarRequestDto;
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
        getProtocolHandler().addAction(GetAvatarsRequestDTO.class, new GetAvatarsAction(getServerWorker(), this));
        getProtocolHandler().addAction(AddAvatarRequestDto.class, new AddAvatarAction(getServerWorker(), this));
        getProtocolHandler().addAction(GetAvatarRequestDTO.class, new GetAvatarAction(getServerWorker(), this));
        getProtocolHandler().addAction(DeleteAvatarRequestDTO.class, new DeleteAvatarAction(getServerWorker(), this));
        getProtocolHandler().addAction(UpdateAvatarRequestDto.class, new UpdateAvatarAction(getServerWorker(), this));
    }

    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {

    }
}
