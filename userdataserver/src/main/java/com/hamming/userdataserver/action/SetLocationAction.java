package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.server.Database;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.SetAvatarRequestDto;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.SetAvatarResponseDto;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.SetLocationDto;
import com.hamming.storim.server.common.model.Location;
import com.hamming.userdataserver.DTOFactory;
import com.hamming.userdataserver.factories.AvatarFactory;
import com.hamming.userdataserver.factories.LocationFactory;
import com.hamming.userdataserver.factories.UserFactory;
import com.hamming.userdataserver.model.Avatar;
import com.hamming.userdataserver.model.User;

public class SetLocationAction extends Action<SetLocationDto> {
    private ServerWorker serverWorker;

    public SetLocationAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        SetLocationDto dto = getDto();
        Location location = LocationFactory.getInstance().getLocationForObject(dto.getObjectId());

        if (dto.getLocation() == null) {
            //Remove location!
            Database.getInstance().removeBasicObject(location);
        } else {
            if (location == null) {
                location = LocationFactory.getInstance().createLocation(null, dto.getLocation());
            }
            location.setServerId(dto.getLocation().getServerId());
            location.setRoomId(dto.getLocation().getRoomId());
            location.setX(dto.getLocation().getX());
            location.setY(dto.getLocation().getY());
            LocationFactory.getInstance().setLocation(dto.getObjectId(), location);
        }
    }

}
