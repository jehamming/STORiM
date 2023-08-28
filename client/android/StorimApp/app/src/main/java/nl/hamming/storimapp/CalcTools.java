package nl.hamming.storimapp;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.protocol.request.MovementRequestDTO;

public class CalcTools {

    private static final float RUN_SPEED = 1;
    private static final float TURN_SPEED = 7;


    public static LocationDto calculateNewPosition(MovementRequestDTO request, LocationDto location) {
        float currentSpeed = 0;
        if (request.isForward()) {
            currentSpeed = RUN_SPEED;
        } else if (request.isBack()) {
            currentSpeed = -RUN_SPEED;
        }
        float currentTurnSpeed = 0;
        if (request.isRight()) {
            currentTurnSpeed = -TURN_SPEED;
        } else if (request.isLeft()) {
            currentTurnSpeed = TURN_SPEED;
        }

        location = calculateNewPosition(location, currentSpeed);

        return location;
    }

    public static LocationDto calculateNewPosition(LocationDto location, float currentSpeed) {
        // Calculate new position

        location.setX((int) (location.getX() + currentSpeed ));
        location.setY((int) (location.getY() + currentSpeed ));

        return location;
    }

}
