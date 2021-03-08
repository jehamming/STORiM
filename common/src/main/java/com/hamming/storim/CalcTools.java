package com.hamming.storim;

import com.hamming.storim.model.dto.protocol.MovementRequestDTO;
import com.hamming.storim.model.dto.LocationDto;

public class CalcTools {

    private static final int RUN_SPEED = 5;


    public static LocationDto calculateNewPosition(MovementRequestDTO request, LocationDto location) {
        if (request.isForward()) {
            location.setY(location.getY() - RUN_SPEED);
        }
        if (request.isBack()) {
            location.setY(location.getY() + RUN_SPEED);
        }
        if (request.isRight()) {
            location.setX(location.getX() + RUN_SPEED );
        }
        if (request.isLeft()) {
            location.setX(location.getX() - RUN_SPEED );
        }
        return location;
    }

}
