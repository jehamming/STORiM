package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.ServerRegistrationDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetRoomsForServerDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetRoomsForServerResponseDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetServerRegistrationsDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetServerRegistrationsResponseDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.Room;

import java.util.HashMap;
import java.util.List;

public class GetRoomsForServerAction extends Action<GetRoomsForServerDTO> {


    public GetRoomsForServerAction(ClientConnection client) {
        super(client);
    }


    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        HashMap<Long, String> rooms = new HashMap<>();


        String serverName = getDto().getServerName();
        if ( serverName.equals( client.getServer().getServerName())) {
            //This server!
            for (Room room : RoomFactory.getInstance().getRooms()) {
                rooms.put(room.getId(), room.getName());
            }
        } else {
            // Get Rooms via LoginServer
            rooms = client.getServer().getLoginServerProxy().getRoomsForServer(getDto().getServerName());
        }

        GetRoomsForServerResponseDTO getRoomsForServerResponseDTO = new GetRoomsForServerResponseDTO(rooms);

        getClient().send(getRoomsForServerResponseDTO);
    }
}