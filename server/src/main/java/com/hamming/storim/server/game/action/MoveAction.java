package com.hamming.storim.server.game.action;

import com.hamming.storim.server.ClientConnection;
import com.hamming.storim.server.game.GameConstants;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.model.Room;
import com.hamming.storim.server.model.User;
import com.hamming.storim.server.model.Location;
import com.hamming.storim.common.dto.protocol.MovementRequestDTO;

public class MoveAction extends Action<MovementRequestDTO> {
    private GameController controller;
    private ClientConnection client;

    public MoveAction(GameController controller, ClientConnection client) {
        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        User u = client.getCurrentUser();
        handleMoveRequest(getDto().getSequence(), u, getDto().isForward(), getDto().isBack(), getDto().isLeft(), getDto().isRight());
    }

    public void handleMoveRequest(Long sequence, User u, boolean forward, boolean back, boolean left, boolean right) {
        Location location = u.getLocation();
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

            location.setSequence(sequence);

            controller.setLocation(u,location);
        }
    }

    private void checkRoomBounds(Location l) {
        Room r = l.getRoom();
        //TODO Check room bounds using the TILE size
//        int maxlen = GameConstants.TILE_SIZE * r.getSize();
//        if ( l.getX() > maxlen) {
//            l.setX(maxlen);
//        }
//        if ( l.getX() < 0 ) {
//            l.setX(0);
//        }
//        if ( l.getY() > maxlen) {
//            l.setY(maxlen);
//        }
//        if ( l.getY() < 0 ) {
//            l.setY(0);
//        }
    }


}
