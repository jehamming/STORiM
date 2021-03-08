package nl.hamming.storimapp;

import com.hamming.storim.model.dto.MovementDto;
import com.hamming.storim.model.dto.UserLocationDto;

public class CalcTools {

    private static final float RUN_SPEED = 1;
    private static final float TURN_SPEED = 7;


    public static UserLocationDto calculateNewPosition(MovementDto request, UserLocationDto location) {
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

    public static UserLocationDto calculateNewPosition(UserLocationDto location, float currentSpeed) {
        // Calculate new position

        location.setX(location.getX() + currentSpeed );
        location.setY(location.getY() + currentSpeed );

        return location;
    }

}
