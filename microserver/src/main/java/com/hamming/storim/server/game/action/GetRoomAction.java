package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.requestresponse.GetRoomDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetRoomResultDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.DTOFactory;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.game.GameController;

public class GetRoomAction extends Action<GetRoomDTO> {
    private GameController controller;


    public GetRoomAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;

    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        Room bp = RoomFactory.getInstance().findRoomByID(getDto().getRoomID());
        if ( bp != null ) {
            client.sendRoom(bp);
        } else {
            GetRoomResultDTO getRoomResultDTO = DTOFactory.getInstance().getRoomResultDTO(false, "Room not found!", null);
            getClient().send(getRoomResultDTO);
        }
    }

}
