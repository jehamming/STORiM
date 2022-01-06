package com.hamming.storim.server.game;

import com.hamming.storim.common.dto.protocol.MovementRequestDTO;
import com.hamming.storim.common.dto.protocol.TeleportRequestDTO;
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
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ProtocolHandler;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.action.*;

public class GameProtocolHandler extends ProtocolHandler<Action> {

    private GameController controller;
    private STORIMClientConnection client;

    public GameProtocolHandler(GameController controller, STORIMClientConnection client) {
        super();
        this.controller = controller;
        this.client = client;
    }

    public void addActions() {
        addAction(MovementRequestDTO.class, new MoveAction(controller,client));
        addAction(TeleportRequestDTO.class, new TeleportAction(controller,client));
        addAction(GetRoomDTO.class, new GetRoomAction(controller,client));
        addAction(GetUserDTO.class, new GetUserAction(controller, client));
        addAction(GetVerbDTO.class, new GetVerbAction(controller, client));
        addAction(ExecVerbDTO.class, new ExecVerbAction(controller, client));
        addAction(AddVerbDto.class, new AddVerbAction(controller, client));
        addAction(UpdateVerbDto.class, new UpdateVerbAction(controller, client));
        addAction(DeleteVerbDTO.class, new DeleteVerbAction(controller, client));
        addAction(AddRoomDto.class, new AddRoomAction(controller, client));
        addAction(UpdateRoomDto.class, new UpdateRoomAction(controller, client));
        addAction(DeleteRoomDTO.class, new DeleteRoomAction(controller, client));
        addAction(AddAvatarDto.class, new AddAvatarAction(controller, client));
        addAction(UpdateUserDto.class, new UpdateUserAction(controller, client));
        addAction(DeleteAvatarDTO.class, new DeleteAvatarAction(controller, client));
        addAction(UpdateAvatarDto.class, new UpdateAvatarAction(controller, client));
        addAction(AddThingDto.class, new AddThingAction(controller, client));
        addAction(DeleteThingDTO.class, new DeleteThingAction(controller, client));
        addAction(UpdateThingDto.class, new UpdateThingAction(controller, client));
        addAction(PlaceThingInRoomRequestDTO.class, new PlaceThingInRoomAction(controller, client));
        addAction(UpdateThingLocationDto.class, new UpdateThingLocationAction(controller, client));
    }

}
