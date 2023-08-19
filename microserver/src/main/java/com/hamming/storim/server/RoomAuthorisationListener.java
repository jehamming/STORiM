package com.hamming.storim.server;

import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.protocol.serverpush.RoomAddedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.RoomDeletedDTO;
import com.hamming.storim.common.net.Server;
import com.hamming.storim.server.common.model.Room;

import java.util.List;

public class RoomAuthorisationListener extends AuthorisationListener<Room> {

    private Server server;
    private STORIMClientConnection client;

    public RoomAuthorisationListener(Server server, STORIMClientConnection client) {
        this.server = server;
        this.client = client;
    }
    @Override
    public void authorisationChanged(Room r, List<Long> old) {
        AuthorisationDelta delta = server.getAuthorisationController().getAuthorisationDelta(old, r.getEditors());
        if ( delta.getAdded().contains(client.getCurrentUser().getId())) {
            // Send TileSet!
            RoomDto roomDto = DTOFactory.getInstance().getRoomDto(r, client.getServer().getServerURI());
            RoomAddedDTO roomAddedDTO = new RoomAddedDTO(roomDto);
            client.send(roomAddedDTO);
        }
        if ( delta.getRemoved().contains(client.getCurrentUser().getId())) {
            // Remove TileSet!
            RoomDeletedDTO roomDeletedDTO = new RoomDeletedDTO(r.getId());
            client.send(roomDeletedDTO);
        }
    }
}
