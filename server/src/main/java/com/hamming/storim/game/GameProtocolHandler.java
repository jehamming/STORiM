package com.hamming.storim.game;

import com.hamming.storim.ClientConnection;
import com.hamming.storim.game.action.*;
import com.hamming.storim.model.dto.*;
import com.hamming.storim.model.dto.protocol.*;

import java.util.HashMap;
import java.util.Map;

public class GameProtocolHandler implements Protocol {

    private GameController controller;
    private ClientConnection client;
    private Map<Class, Action> actions;

    public GameProtocolHandler(GameController controller, ClientConnection client) {
        this.controller = controller;
        this.client = client;
        actions = new HashMap<Class, Action>();
        actions.put(VersionCheckDTO.class, new ProtocolVersionAction(controller, client));
    }

    public void protocolVersionCompatible() {
        emptyCommands();
        actions.put(LoginRequestDTO.class, new LoginAction(controller, client));
    }

    public void LoggedIn() {
        actions.put(MovementRequestDTO.class, new MoveAction(controller,client));
        actions.put(TeleportRequestDTO.class, new TeleportAction(controller,client));
        actions.put(GetRoomDTO.class, new GetRoomAction(controller,client));
        actions.put(GetUserDTO.class, new GetUserAction(controller, client));
        actions.put(GetVerbDTO.class, new GetVerbAction(controller, client));
        actions.put(ExecVerbDTO.class, new ExecVerbAction(controller, client));
        actions.put(AddVerbDto.class, new AddVerbAction(controller, client));
        actions.put(UpdateVerbDto.class, new UpdateVerbAction(controller, client));
        actions.put(DeleteVerbDTO.class, new DeleteVerbAction(controller, client));
        actions.put(AddRoomDto.class, new AddRoomAction(controller, client));
        actions.put(UpdateRoomDto.class, new UpdateRoomAction(controller, client));
        actions.put(DeleteRoomDTO.class, new DeleteRoomAction(controller, client));
    }

    private void emptyCommands() {
        for (Class c : actions.keySet() ) {
            actions.remove(c);
        }
    }

    public void reset() {
        emptyCommands();
        actions.put(VersionCheckDTO.class, new ProtocolVersionAction(controller, client));
    }


    public Action getAction(DTO dto) {
        return actions.get(dto.getClass());
    }


}
