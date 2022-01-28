package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.request.MovementRequestDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.model.Location;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.common.model.User;
import com.hamming.storim.server.game.GameConstants;
import com.hamming.storim.server.game.GameController;

public class MoveAction extends Action<MovementRequestDTO> {
    private GameController controller;

    public MoveAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        User u = client.getCurrentUser();
        handleMoveRequest(getDto().getSequence(), u, getDto().isForward(), getDto().isBack(), getDto().isLeft(), getDto().isRight());
    }

    public void handleMoveRequest(Long sequence, User u, boolean forward, boolean back, boolean left, boolean right) {
        Location location = controller.getGameState().getLocation(u.getId());
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

            controller.userLocationUpdated(getClient(), u);
        }
    }

    private void checkRoomBounds(Location l) {
        Room r = l.getRoom();
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
