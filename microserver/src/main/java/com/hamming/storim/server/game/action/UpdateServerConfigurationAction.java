package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.ServerConfigurationDTO;
import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.request.UpdateRoomDto;
import com.hamming.storim.common.dto.protocol.request.UpdateServerConfigurationDTO;
import com.hamming.storim.common.dto.protocol.serverpush.RoomUpdatedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.ServerConfigurationUpdatedDTO;
import com.hamming.storim.server.DTOFactory;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.ServerConfiguration;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.factories.TileSetFactory;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.common.model.TileSet;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.game.RoomEvent;

public class UpdateServerConfigurationAction extends Action<UpdateServerConfigurationDTO> {
    private GameController controller;

    public UpdateServerConfigurationAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        UpdateServerConfigurationDTO dto = getDto();

        ServerConfiguration serverConfiguration = client.getServer().getServerConfiguration();
        if (client.isAuthorized(serverConfiguration)) {
            Long tileSetId = dto.getDefaultTileSetId();
            TileSet tileSet = TileSetFactory.getInstance().findTileSetById(tileSetId);
            if ( tileSet != null ) {
                serverConfiguration.setDefaultTileSet(tileSet);
                serverConfiguration.setDefaultTile(dto.getDefaultTile());
                Room defaultRoom = RoomFactory.getInstance().findRoomByID(dto.getDefaultRoomId());
                if ( defaultRoom != null ) {
                    serverConfiguration.setDefaultRoom(defaultRoom);
                }
                serverConfiguration.setServerAdmins(dto.getServerAdmins());
            } else {
                ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), "TileSet " + tileSetId + " not found");
                client.send(errorDTO);
            }
        } else {
            ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), "Not authorized!");
            client.send(errorDTO);
        }

        //TODO send this to all other admin clients
//        ServerConfigurationDTO serverConfigurationDTO = DTOFactory.getInstance().getServerConfigurationDTO(serverConfiguration);
//        ServerConfigurationUpdatedDTO serverConfigurationUpdatedDTO = new ServerConfigurationUpdatedDTO(serverConfigurationDTO);
//        client.send(serverConfigurationUpdatedDTO);
    }


}
