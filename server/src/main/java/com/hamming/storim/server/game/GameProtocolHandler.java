package com.hamming.storim.server.game;

import com.hamming.storim.common.Protocol;
import com.hamming.storim.common.dto.protocol.*;
import com.hamming.storim.common.dto.protocol.avatar.AddAvatarDto;
import com.hamming.storim.common.dto.protocol.avatar.DeleteAvatarDTO;
import com.hamming.storim.common.dto.protocol.avatar.UpdateAvatarDto;
import com.hamming.storim.common.dto.protocol.room.AddRoomDto;
import com.hamming.storim.common.dto.protocol.room.DeleteRoomDTO;
import com.hamming.storim.common.dto.protocol.room.GetRoomDTO;
import com.hamming.storim.common.dto.protocol.room.UpdateRoomDto;
import com.hamming.storim.common.dto.protocol.thing.*;
import com.hamming.storim.common.dto.protocol.user.GetUserDTO;
import com.hamming.storim.common.dto.protocol.user.UpdateUserDto;
import com.hamming.storim.common.dto.protocol.verb.*;
import com.hamming.storim.server.ClientConnection;
import com.hamming.storim.server.game.action.*;

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
        actions.put(AddAvatarDto.class, new AddAvatarAction(controller, client));
        actions.put(UpdateUserDto.class, new UpdateUserAction(controller, client));
        actions.put(DeleteAvatarDTO.class, new DeleteAvatarAction(controller, client));
        actions.put(UpdateAvatarDto.class, new UpdateAvatarAction(controller, client));
        actions.put(AddThingDto.class, new AddThingAction(controller, client));
        actions.put(DeleteThingDTO.class, new DeleteThingAction(controller, client));
        actions.put(UpdateThingDto.class, new UpdateThingAction(controller, client));
        actions.put(PlaceThingInRoomRequestDTO.class, new PlaceThingInRoomAction(controller, client));
        actions.put(UpdateThingLocationDto.class, new UpdateThingLocationAction(controller, client));
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


    public Action getAction(ProtocolDTO dto) {
        return actions.get(dto.getClass());
    }


}
