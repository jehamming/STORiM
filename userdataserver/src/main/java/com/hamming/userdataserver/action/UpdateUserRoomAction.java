package com.hamming.userdataserver.action;

import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.UpdateUserRoomDto;

public class UpdateUserRoomAction extends Action<UpdateUserRoomDto> {

    private ServerWorker serverWorker;

    public UpdateUserRoomAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        Long userId = getDto().getUserId();
        Long roomId = getDto().getRoomId();
        //TODO Implement
        // How to store the room when the dataserver does not know about rooms?

    }
}