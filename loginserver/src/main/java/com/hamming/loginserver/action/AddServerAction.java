package com.hamming.loginserver.action;

import com.hamming.loginserver.LoginServerWorker;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.loginserver.AddServerRequestDTO;

public class AddServerAction extends Action<AddServerRequestDTO> {

    private LoginServerWorker serverWorker;

    public AddServerAction(LoginServerWorker serverWorker) {
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        AddServerRequestDTO request = getDto();
        String error = serverWorker.addServer(request.getName(), request.getUrl(), request.getPort());

        //TODO Make SYNC reply
        if ( error != null ) {
            System.out.println(getClass().getSimpleName()+"-ERROR:" + error);
        }
    }
}