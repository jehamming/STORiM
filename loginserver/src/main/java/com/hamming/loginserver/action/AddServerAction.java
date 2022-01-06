package com.hamming.loginserver.action;

import com.hamming.loginserver.LoginServerWorker;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.loginserver.AddServerRequestDTO;
import com.hamming.storim.server.common.dto.protocol.loginserver.AddServerResponseDTO;

public class AddServerAction extends Action<AddServerRequestDTO> {

    private LoginServerWorker serverWorker;
    private int connectionHashcode;

    public AddServerAction(LoginServerWorker serverWorker, int connectionHashcode) {
        this.serverWorker = serverWorker;
        this.connectionHashcode = connectionHashcode;
    }

    @Override
    public void execute() {
        boolean success = false;
        AddServerRequestDTO request = getDto();
        String error = serverWorker.addServer(connectionHashcode, request.getName(), request.getUrl(), request.getPort());

        if ( error == null ) {
            success = true;
        } else {
            System.out.println("ERROR:("+getClass().getSimpleName()+"):" + error);
        }

        AddServerResponseDTO responseDTO = new AddServerResponseDTO(success, error);
        setResult(responseDTO);
    }
}