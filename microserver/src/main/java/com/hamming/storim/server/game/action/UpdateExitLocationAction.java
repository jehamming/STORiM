package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.ExitDto;
import com.hamming.storim.common.dto.protocol.request.UpdateExitLocationDto;
import com.hamming.storim.common.dto.protocol.serverpush.ExitLocationUpdatedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.LocationUpdateDTO;
import com.hamming.storim.common.dto.protocol.serverpush.MessageInRoomDTO;
import com.hamming.storim.server.DTOFactory;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.ExitFactory;
import com.hamming.storim.server.common.model.Exit;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.game.RoomEvent;

public class UpdateExitLocationAction extends Action<UpdateExitLocationDto> {
    private GameController gameController;


    public UpdateExitLocationAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.gameController = controller;

    }

    @Override
    public void execute() {

        //TODO Are you even allowed to move this Exit? Access control!

        UpdateExitLocationDto dto = getDto();
        STORIMClientConnection client = (STORIMClientConnection) getClient();

        Exit exit = ExitFactory.getInstance().findExitById(dto.getExitId());
        exit.setX(dto.getX());
        exit.setY(dto.getY());

        // Send updated location
        ExitLocationUpdatedDTO exitLocationUpdatedDTO = new ExitLocationUpdatedDTO(exit.getId(), exit.getX(), exit.getY());
        client.send(exitLocationUpdatedDTO);

        MessageInRoomDTO messageInRoomDTO = new MessageInRoomDTO(client.getCurrentUser().getId(), MessageInRoomDTO.Type.USER, "You move exit " + exit.getName());
        client.send(messageInRoomDTO);

        ExitDto exitDto = DTOFactory.getInstance().getExitDTO(exit);

        gameController.fireRoomEvent(client, dto.getRoomId(), new RoomEvent(RoomEvent.Type.EXITLOCATIONUPDATE, exitDto, client.getCurrentUser()));

    }


}
