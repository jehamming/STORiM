package com.hamming.loginserver.action;

import com.hamming.loginserver.LoginServerWorker;
import com.hamming.loginserver.ServerRegistration;
import com.hamming.loginserver.UserClientConnection;
import com.hamming.storim.common.dto.protocol.requestresponse.*;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;

import java.util.HashMap;

public class GetRoomsForServerAction extends Action<GetRoomsForServerDTO> {

    private LoginServerWorker serverWorker;

    public GetRoomsForServerAction(LoginServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        ServerRegistration serverRegistration = serverWorker.findServerRegistration(getDto().getServerName());
        HashMap<Long, String> rooms = null;
        if ( serverRegistration != null ) {
            // Get the rooms from the MicroServer!
            ClientConnection connection = serverRegistration.getConnection();
            GetRoomsResultDTO getRoomsResultDTO =  connection.sendReceive(new GetRoomsDTO(), GetRoomsResultDTO.class);
            if ( getRoomsResultDTO != null ) {
                rooms = getRoomsResultDTO.getRooms();
            }
        }
        GetRoomsForServerResponseDTO response = new GetRoomsForServerResponseDTO(rooms);
        getClient().send(response);
    }
}