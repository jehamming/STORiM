package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.request.MovementRequestDTO;
import com.hamming.storim.common.dto.protocol.serverpush.LocationUpdateDTO;
import com.hamming.storim.server.DTOFactory;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.Location;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.game.GameConstants;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.game.RoomEvent;

public class MoveAction extends Action<MovementRequestDTO> {
    private GameController controller;

    public MoveAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        UserDto u = client.getCurrentUser();
        handleMoveRequest(getDto().getSequence(), u, getDto().isForward(), getDto().isBack(), getDto().isLeft(), getDto().isRight());
    }

    public void handleMoveRequest(Long sequence, UserDto u, boolean forward, boolean back, boolean left, boolean right) {
        Location location = controller.getGameState().getUserLocation(u.getId());
        if (location != null) {
            if (forward) {
                location.setY(location.getY() - GameConstants.RUN_SPEED);
            }
            if (back) {
                location.setY(location.getY() + GameConstants.RUN_SPEED);
            }
            if (right) {
                location.setX(location.getX() + GameConstants.RUN_SPEED );
            }
            if (left) {
                location.setX(location.getX() - GameConstants.RUN_SPEED );
            }

            checkRoomBounds(location);
            userLocationUpdated(getClient(), u, sequence);
        }
    }

    public void userLocationUpdated(ClientConnection source, UserDto u, Long sequence) {
        Location location = controller.getGameState().getUserLocation(u.getId());
        controller.fireRoomEvent(source, location.getRoomId(), new RoomEvent(RoomEvent.Type.USERLOCATIONUPDATE, u));
        LocationDto locationDto = DTOFactory.getInstance().getLocationDTO(location);
        LocationUpdateDTO locationUpdateDTO = new LocationUpdateDTO(u.getId(), locationDto);
        locationUpdateDTO.setSequenceNumber(sequence);
        getClient().send(locationUpdateDTO);
    }

    private void checkRoomBounds(Location l) {
        Room r = RoomFactory.getInstance().findRoomByID(l.getRoomId());
        if ( l.getX() > r.getWidth()) {
            l.setX(r.getWidth());
        }
        if ( l.getX() < 0 ) {
            l.setX(0);
        }
        if ( l.getY() > r.getLength()) {
            l.setY(r.getLength());
        }
        if ( l.getY() < 0 ) {
            l.setY(0);
        }
    }


}
