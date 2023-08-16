package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.VerbDetailsDTO;
import com.hamming.storim.common.dto.protocol.request.ExecVerbDTO;
import com.hamming.storim.common.dto.protocol.serverpush.MessageInRoomDTO;
import com.hamming.storim.common.util.StringUtils;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.Location;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.game.GameConstants;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.game.RoomEvent;

import java.util.HashMap;
import java.util.Map;

public class ExecVerbAction extends Action<ExecVerbDTO> {
    private GameController controller;


    public ExecVerbAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        UserDto u = client.getCurrentUser();
        VerbDetailsDTO verbDetailsDTO = client.getServer().getUserDataServerProxy().getVerb(getDto().getVerbId());
        if (verbDetailsDTO != null ) {
            MessageInRoomDTO messageInRoomDTO = executeVeb(getClient(), u, verbDetailsDTO, getDto().getInput());
            getClient().send(messageInRoomDTO);
        }
    }

    public MessageInRoomDTO executeVeb(ClientConnection source, UserDto u, VerbDetailsDTO verb, String input) {
        Map<String, String> replacements = new HashMap<>();
        Location location = controller.getGameState().getUserLocation(u.getId());
        Room room = RoomFactory.getInstance().findRoomByID(location.getRoomId());
        replacements.put(GameConstants.CMD_REPLACE_CALLER, u.getName());
        replacements.put(GameConstants.CMD_REPLACE_LOCATION, room.getName());
        replacements.put(GameConstants.CMD_REPLACE_MESSAGE, input);

        String toCaller = StringUtils.replace(verb.getToCaller(), replacements);
        String toLocation = StringUtils.replace(verb.getToLocation(), replacements);
        MessageInRoomDTO messageInRoomDTO = new MessageInRoomDTO(u.getId(), MessageInRoomDTO.sType.USER, toCaller, MessageInRoomDTO.mType.VERB );
        controller.fireRoomEvent(source, location.getRoomId(), new RoomEvent(RoomEvent.Type.MESSAGEINROOM, messageInRoomDTO, toLocation));
        return messageInRoomDTO;
    }


}
