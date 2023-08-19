package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.ServerConfigurationDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetRoomDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetServerConfigDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetServerConfigResultDTO;
import com.hamming.storim.server.DTOFactory;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.ServerConfiguration;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.game.GameController;

public class GetServerConfigurationAction extends Action<GetServerConfigDTO> {
    private GameController controller;


    public GetServerConfigurationAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;

    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        ServerConfiguration serverConfiguration = client.getServer().getServerConfiguration();
        Long id = client.getCurrentUser().getId();
        if (client.isAdmin() || serverConfiguration.getServerAdmins().contains(id)) {
            ServerConfigurationDTO dto = DTOFactory.getInstance().getServerConfigurationDTO(serverConfiguration);
            GetServerConfigResultDTO result = new GetServerConfigResultDTO(dto);
            client.send(result);
        }
    }

}
