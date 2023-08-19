package com.hamming.storim.server;

import com.hamming.storim.common.dto.TileSetDto;
import com.hamming.storim.common.dto.protocol.serverpush.TileSetAddedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.TileSetDeletedDTO;
import com.hamming.storim.common.net.Server;
import com.hamming.storim.server.common.model.TileSet;

import java.util.List;

public class TileSetAuthorisationListener extends AuthorisationListener<TileSet> {

    private Server server;
    private STORIMClientConnection client;

    public TileSetAuthorisationListener(Server server, STORIMClientConnection client) {
        this.server = server;
        this.client = client;
    }
    @Override
    public void authorisationChanged(TileSet ts, List<Long> old) {
        AuthorisationDelta delta = server.getAuthorisationController().getAuthorisationDelta(old, ts.getEditors());
        if ( delta.getAdded().contains(client.getCurrentUser().getId())) {
            // Send TileSet!
            TileSetDto tileSetDto = DTOFactory.getInstance().getTileSetDTO(ts);
            TileSetAddedDTO tileSetAddedDTO = new TileSetAddedDTO(tileSetDto);
            client.send(tileSetAddedDTO);
        }
        if ( delta.getRemoved().contains(client.getCurrentUser().getId())) {
            // Remove TileSet!
            TileSetDeletedDTO tileSetDeletedDTO = new TileSetDeletedDTO(ts.getId());
            client.send(tileSetDeletedDTO);
        }
    }
}
