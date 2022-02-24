package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.ExitDto;
import com.hamming.storim.common.dto.protocol.requestresponse.GetExitDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetExitResponseDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.DTOFactory;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.Exit;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.game.GameController;

public class GetExitAction extends Action<GetExitDTO> {
    private GameController controller;

    public GetExitAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        GetExitResponseDTO result;
        Room bp = RoomFactory.getInstance().findRoomByID(getDto().getRoomID());
        if ( bp != null ) {
            Exit exit = bp.getExit(getDto().getExitID());
            ExitDto exitDto = DTOFactory.getInstance().getExitDTO(exit);
            result = new GetExitResponseDTO(true, null, exitDto);
        } else {
            result = new GetExitResponseDTO(false, "Room not found!", null);
        }
        getClient().send(result);
    }

}
