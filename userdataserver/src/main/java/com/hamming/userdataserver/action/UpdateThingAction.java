package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.UpdateAvatarRequestDto;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.UpdateAvatarResponseDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.UpdateThingRequestDto;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.UpdateThingResponseDTO;
import com.hamming.userdataserver.DTOFactory;
import com.hamming.userdataserver.factories.AvatarFactory;
import com.hamming.userdataserver.factories.ThingFactory;
import com.hamming.userdataserver.model.Avatar;
import com.hamming.userdataserver.model.Thing;

public class UpdateThingAction extends Action<UpdateThingRequestDto> {
    private ServerWorker serverWorker;


    public UpdateThingAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        UpdateThingRequestDto dto = getDto();

        String errorMessage = "";
        ThingDto thingDto = null;
        boolean success = false;

        Long thingId = dto.getThingId();
        Thing thing = ThingFactory.getInstance().findThingById(thingId);
        if (thing != null) {
            thing.setName(dto.getName());
            thing.setDescription(dto.getDescription());
            thing.setScale(dto.getScale());
            thing.setRotation(dto.getRotation());
            thing.setImage(ImageUtils.decode(dto.getImageData()));
            thingDto = DTOFactory.getInstance().getThingDTO(thing);
            success = true;
        } else {
            errorMessage = dto.getClass().getSimpleName() + ": Thing " + thingId + " not found!";
        }
        UpdateThingResponseDTO reponse = new UpdateThingResponseDTO(success, errorMessage, thingDto);
        getClient().send(reponse);
    }

}
