package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.request.DeleteExitDTO;
import com.hamming.storim.common.dto.protocol.request.DeleteThingDTO;
import com.hamming.storim.common.dto.protocol.serverpush.ExitDeletedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.ThingDeletedDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.ExitFactory;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.Exit;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.game.GameController;

public class DeleteExitAction extends Action<DeleteExitDTO> {
    private GameController controller;


    public DeleteExitAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;

    }

    @Override
    public void execute() {
        DeleteExitDTO dto = getDto();
        STORIMClientConnection client = (STORIMClientConnection) getClient();

        Exit exit = ExitFactory.getInstance().findExitById(dto.getExitID());
        if ( exit != null ) {
            Room room = client.getCurrentRoom();
            room.removeExit(exit);

            ExitFactory.getInstance().deleteExit(exit);

            ExitDeletedDTO exitDeletedDTO = new ExitDeletedDTO(exit.getId());
            client.send(exitDeletedDTO);
        }
    }

}
