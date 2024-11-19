package com.hamming.storim.server.engine.action;

import com.hamming.storim.common.dto.ExitDto;
import com.hamming.storim.common.dto.protocol.request.UpdateExitDto;
import com.hamming.storim.common.dto.protocol.serverpush.ExitUpdatedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.MessageInRoomDTO;
import com.hamming.storim.server.DTOFactory;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.ExitFactory;
import com.hamming.storim.server.common.model.Exit;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.engine.GameController;
import com.hamming.storim.server.engine.RoomEvent;

public class UpdateExitAction extends Action<UpdateExitDto> {
    private GameController gameController;


    public UpdateExitAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.gameController = controller;

    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        UpdateExitDto dto = getDto();

        Exit exit = ExitFactory.getInstance().findExitById(dto.getId());
        if ( exit != null ) {
            Room room = client.getCurrentRoom();
            exit.setName(dto.getName());
            exit.setDescription(dto.getDescription());
            exit.setToRoomURI(dto.getToRoomURI());
            exit.setToRoomID(dto.getToRoomId());
            exit.setScale(dto.getScale());
            exit.setRotation(dto.getRotation());
            exit.setImage(ImageUtils.decode(dto.getImageData()));
            ExitFactory.getInstance().updateExit(exit);

            ExitDto exitDto = DTOFactory.getInstance().getExitDTO(exit);
            ExitUpdatedDTO exitUpdatedDTO = new ExitUpdatedDTO(exitDto);
            client.send(exitUpdatedDTO);

            String txt = " You change exit " + exitDto.getName();
            MessageInRoomDTO messageInRoomDTO = new MessageInRoomDTO(exitDto.getId(), MessageInRoomDTO.sType.USER, txt, MessageInRoomDTO.mType.UPDATE);
            client.send(messageInRoomDTO);

            gameController.fireRoomEvent(client, room.getId(), new RoomEvent(RoomEvent.Type.EXITUPDATED, exitDto, client.getCurrentUser()));

        }
    }

}
