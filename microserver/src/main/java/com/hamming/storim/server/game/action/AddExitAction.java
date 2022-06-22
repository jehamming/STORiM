package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.ExitDto;
import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.request.AddExitDto;
import com.hamming.storim.common.dto.protocol.serverpush.ExitAddedDTO;
import com.hamming.storim.server.DTOFactory;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.ExitFactory;
import com.hamming.storim.server.common.factories.LocationFactory;
import com.hamming.storim.server.common.model.Exit;
import com.hamming.storim.server.common.model.Location;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.game.RoomEvent;

import java.awt.*;

public class AddExitAction extends Action<AddExitDto> {
    private GameController gameController;


    public AddExitAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.gameController = controller;

    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        AddExitDto dto = getDto();
        String name = dto.getName();
        String description = dto.getDescription();
        float scale = dto.getScale();
        int rotation = dto.getRotation();
        Long toRoomID = dto.getToRoomID();
        String toServerID = dto.getToServerID();
        Image image = ImageUtils.decode(dto.getImageData());
        UserDto creator = client.getCurrentUser();
        Room currentRoom = client.getCurrentRoom();
        String serverName = client.getServer().getServerName();

        Exit exit = ExitFactory.getInstance().createExit(creator.getId(), name, toRoomID, toServerID, description, scale, rotation, image );
        // Add to Room
        currentRoom.addExit(exit);
        // Place in Room
        Location location = new Location(exit.getId(), serverName, currentRoom.getId(),currentRoom.getSpawnPointX(), currentRoom.getSpawnPointY());
        LocationFactory.getInstance().setLocation(exit.getId(), location);

        LocationDto locationDto = DTOFactory.getInstance().getLocationDTO(location);

        if ( exit != null ) {
            ExitDto exitDto = DTOFactory.getInstance().getExitDTO(exit);
            ExitAddedDTO exitAddedDTO = new ExitAddedDTO(exitDto, locationDto);
            client.send(exitAddedDTO);

            gameController.fireRoomEvent(client, currentRoom.getId(), new RoomEvent(RoomEvent.Type.EXITADDED, exitDto, locationDto));
        }
    }
}
