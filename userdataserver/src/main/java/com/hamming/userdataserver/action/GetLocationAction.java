package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.GetLocationDto;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.GetLocationResponseDto;
import com.hamming.storim.server.common.model.Location;
import com.hamming.userdataserver.DTOFactory;
import com.hamming.storim.server.common.factories.LocationFactory;

public class GetLocationAction extends Action<GetLocationDto> {
    private ServerWorker serverWorker;

    public GetLocationAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        GetLocationDto dto = getDto();
        LocationDto locationDto = null;
        Location location  = LocationFactory.getInstance().getLocationForObject(dto.getObjectId());
        if ( location != null ) {
            locationDto = DTOFactory.getInstance().getLocationDTO(location);
        }
        GetLocationResponseDto getLocationResponseDto = new GetLocationResponseDto(dto.getObjectId(), locationDto);

        getClient().send(getLocationResponseDto);


    }

}
