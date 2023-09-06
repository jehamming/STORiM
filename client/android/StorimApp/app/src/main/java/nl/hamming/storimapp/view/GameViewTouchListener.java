package nl.hamming.storimapp.view;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class GameViewTouchListener implements View.OnTouchListener {

    private boolean forward, back, left, right;
    private float startX, startY;
    private float THRESHOLD = 20;
    private Context context;
    private GestureDetector gestureDetector;
    private GameView gameView;
    public GameViewTouchListener(Context ctx, GameView gameView) {
        this.context = ctx;
        this.gameView = gameView;
        gameView.setOnTouchListener(this);
        setup();
    }

    private void setup() {
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                gameView.viewTapped(e.getX(), e.getY());
                return true;
            }
        });
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Step 1; check Tap
        if ( !gestureDetector.onTouchEvent(event)) {
            // Step 2: check Drag
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getRawX();
                    startY = event.getRawY();
                    forward = false;
                    back = false;
                    left = false;
                    right = false;
                    break;
                case MotionEvent.ACTION_UP:
                    forward = false;
                    back = false;
                    left = false;
                    right = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    System.out.println("startX:" + startX + ", startY:" + startY + ", getX:" + event.getRawX() + ", getY:" + event.getRawY());
                    float deltaX = event.getRawX() - startX;
                    float deltaY = event.getRawY() - startY;
                    if (Math.abs(deltaX) > THRESHOLD || Math.abs(deltaY) > THRESHOLD) {
                        if (deltaX > 0) {
                            right = true;
                            left = false;
                        } else if (deltaX < 0) {
                            left = true;
                            right = false;
                        }
                        if (deltaY > 0) {
                            back = true;
                            forward = false;
                        } else if (deltaY < 0) {
                            forward = true;
                            back = false;
                        }
                    }
                    System.out.println("Forward:" + forward + ",Back:" + back + ", Left:" + left + ", Right:" + right);
                    break;
            }
        }
        return true;
    }

    public boolean isForward() {
        return forward;
    }

    public boolean isBack() {
        return back;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }
}
