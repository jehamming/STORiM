package nl.hamming.storimapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.SurfaceView;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.ExitDto;
import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.UserDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import nl.hamming.storimapp.ImageUtils;
import nl.hamming.storimapp.R;
import nl.hamming.storimapp.controllers.GameViewController;
import nl.hamming.storimapp.engine.actions.Action;

public class GameView extends SurfaceView implements Runnable {


    //boolean variable to track if the game is playing or not
    volatile boolean playing;
    private int fps = 30;
    private int arrowSize = 120;
    private long time;
    private Bitmap backBuffer;

    //the game thread
    private Thread gameThread = null;

    private List<Action> actions;
    private List<Player> players;
    private List<Thing> things;
    private List<Exit> exits;

    public RoomDto room;
    private int[][] backgroundTileMap;
    private int[][] foregroundTileMap;

    private TileSet backgroundTileSet;
    private TileSet foregroundTileSet;
    private GameViewController gameViewController;
    private Bitmap defaultUserImage;
    private Bitmap arrowForward;
    private Bitmap arrowBack;
    private Bitmap arrowLeft;
    private Bitmap arrowRight;
    private Bitmap speechBalloon;
    private static int SPEECH_BALLOON_TIME = 2000;
    private Timer timer;

    private BasicDrawableObject selectedObject;
    private float unitX = 1f;
    private float unitY = 1f;
    private Long currentUserId;
    private GameViewTouchListener gameViewTouchListener;
    private TimerTask moveRequestTimerTask;

    public GameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        actions = Collections.synchronizedList(new LinkedList<>());
        players = new ArrayList<>();
        things = new ArrayList<>();
        exits = new ArrayList<>();
        timer = new Timer();

        defaultUserImage = BitmapFactory.decodeResource(getResources(), R.drawable.user);
        arrowForward = BitmapFactory.decodeResource(getResources(), R.drawable.arrowforward);
        arrowBack = BitmapFactory.decodeResource(getResources(), R.drawable.arrowback);
        arrowLeft = BitmapFactory.decodeResource(getResources(), R.drawable.arrowleft);
        arrowRight = BitmapFactory.decodeResource(getResources(), R.drawable.arrowright);
        speechBalloon = BitmapFactory.decodeResource(getResources(), R.drawable.speechballoon);
        setWillNotDraw(false);

        gameViewTouchListener = new GameViewTouchListener(context, this);;
    }



    public void viewTapped(float x, float y) {
        selectedObject = getSelectedObject((int) x, (int) y);
        if ( selectedObject != null ) {
            if (selectedObject instanceof Exit) {
                Exit exit = (Exit) selectedObject;
                gameViewController.exitClicked(exit.getId(), exit.getName(), exit.getToRoomURI());
            }
        }
    }

    private class MoveRequestTask extends TimerTask {
        public void run() {
            boolean forward = gameViewTouchListener.isForward();
            boolean back = gameViewTouchListener.isBack();
            boolean left = gameViewTouchListener.isLeft();
            boolean right = gameViewTouchListener.isRight();
            if (forward || back || right || left) {
                gameViewController.sendMoveRequest(forward, back, left, right);
            }
        }
    }



    public void updateExit(ExitDto exitDto) {
        Exit exit = getExit(exitDto.getId());
        if (exit != null) {
            exit.setName(exitDto.getName());
            exit.setToRoomURI(exit.getToRoomURI());
            exit.setImage(ImageUtils.decode(exitDto.getImageData()));
            exit.setScale(exitDto.getScale());
            exit.setRotation(exitDto.getRotation());
        }
    }

    public void addSpeechBalloon(Long sourceID) {
        Player p = getPlayer(sourceID);
        if ( p != null ) {
            p.setTalking(true);
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                p.setTalking(false);
                repaint();
            }
        }, SPEECH_BALLOON_TIME);
        repaint();
    }

    private void repaint() {
        invalidate();
    }

    public void setCurrentUserId(Long id) {
        currentUserId = id;
    }


    public void setgameViewController(GameViewController gameViewController) {
        this.gameViewController = gameViewController;
    }

    private void updatePosition(Thing thing) {
        int translatedX = (int) (thing.getX() / unitX);
        int translatedY = (int) (thing.getY() / unitY);
        gameViewController.updateThingLocationRequest(thing.getId(), translatedX, translatedY);
        repaint();
    }

    private void updatePosition(Exit exit) {
        int translatedX = (int) (exit.getX() / unitX);
        int translatedY = (int) (exit.getY() / unitY);
        gameViewController.updateExitLocationRequest(exit.getId(), translatedX, translatedY);
        repaint();
    }


    @Override
    public void run() {
        moveRequestTimerTask = new MoveRequestTask();
        timer.scheduleAtFixedRate(moveRequestTimerTask, 0, 50);
        while (playing) {
            time = System.currentTimeMillis();
            handleActions();
            waitIfNeeded();
        }
    }


    private void handleActions() {
        synchronized (actions) {
            for (Action action : actions) {
                action.execute();
                repaint();
            }
            actions.removeAll(actions);
        }
        repaint();
    }

    private void addThing(Thing thing) {
        if (!things.contains(thing)) {
            things.add(thing);
        }
    }

    public void addExit(Exit exit) {
        if (!exits.contains(exit)) {
            exits.add(exit);
        }
    }

    public void deleteThing(Thing thing) {
        if (things.contains(thing)) {
            things.remove(thing);
        }
    }

    public void deleteExit(Exit exit) {
        if (exits.contains(exit)) {
            exits.remove(exit);
        }
    }

    public Thing getThing(Long thingId) {
        Thing thing = null;
        for (Thing t : things) {
            if (t.getId().equals(thingId)) {
                thing = t;
                break;
            }
        }
        return thing;
    }

    public Exit getExit(Long id) {
        Exit exit = null;
        for (Exit e : exits) {
            if (e.getId().equals(id)) {
                exit = e;
                break;
            }
        }
        return exit;
    }

    public void addPlayer(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
    }

    public Player getPlayer(Long userId) {
        Player player = null;
        for (Player p : players) {
            if (p.getId().equals(userId)) {
                player = p;
                break;
            }
        }
        return player;
    }

    public RoomDto getRoom() {
        return room;
    }

    public void setRoom(RoomDto room) {
        this.room = room;
    }


    public void setPlayers(List<Player> players) {
        this.players = players;
    }


    public void setThings(ArrayList<Thing> things) {
        this.things = things;
    }

    public void setExits(ArrayList<Exit> exits) {
        this.exits = exits;
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }


    private void waitIfNeeded() {
        time = (1000 / fps) - (System.currentTimeMillis() - time);
        if (time > 0) {
            try {
                Thread.sleep(time);
            } catch (Exception e) {
            }
        }
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void start() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public BasicDrawableObject getSelectedObject(int x, int y) {
        BasicDrawableObject object = null;
        for (BasicDrawableObject player : players) {
            if (player.withinBounds(x, y)) {
                object = player;
                break;
            }
        }
        if (object == null) {
            for (BasicDrawableObject thing : things) {
                if (thing.withinBounds(x, y)) {
                    object = thing;
                    break;
                }
            }
        }
        if (object == null) {
            for (BasicDrawableObject exit : exits) {
                if (exit.withinBounds(x, y)) {
                    object = exit;
                    break;
                }
            }
        }
        return object;
    }

    private void clearSelection() {
        for (BasicDrawableObject thing : things) {
            thing.setSelected(false);
        }
        for (BasicDrawableObject player : players) {
            player.setSelected(false);
        }
        for (BasicDrawableObject exit : exits) {
            exit.setSelected(false);
        }
        selectedObject = null;
    }


    private void drawControls(Canvas c) {
        Paint paint = new Paint();
        Bitmap resizedArrowForward = ImageUtils.resize(arrowForward, arrowSize, arrowSize);
        c.drawBitmap(resizedArrowForward, getWidth() - (2 * arrowSize), getHeight() - (3 * arrowSize), paint);
        Bitmap resizedArrowBack = ImageUtils.resize(arrowBack, arrowSize, arrowSize);
        c.drawBitmap(resizedArrowBack, getWidth() - (2 * arrowSize), getHeight() - (arrowSize), paint);
        Bitmap resizedArrowLeft = ImageUtils.resize(arrowLeft, arrowSize, arrowSize);
        c.drawBitmap(resizedArrowLeft, getWidth() - (3 * arrowSize), getHeight() - (2 * arrowSize), paint);
        Bitmap resizedArrowRight = ImageUtils.resize(arrowRight, arrowSize, arrowSize);
        c.drawBitmap(resizedArrowRight, getWidth() - (arrowSize), getHeight() - (2 * arrowSize), paint);
    }

    public boolean isForwardButton(int x, int y) {
        int startX = getWidth() - (2 * arrowSize);
        int endX = startX + arrowSize;
        int startY = getHeight() - (3 * arrowSize);
        int endY = startY + arrowSize;
        return x > startX && x < endX && y > startY && y < endY;
    }

    public boolean isBackButton(int x, int y) {
        int startX = getWidth() - (2 * arrowSize);
        int endX = startX + arrowSize;
        int startY = getHeight() - (arrowSize);
        int endY = startY + arrowSize;
        return x > startX && x < endX && y > startY && y < endY;
    }

    public boolean isRightButton(int x, int y) {
        int startX = getWidth() - (arrowSize);
        int endX = startX + arrowSize;
        int startY = getHeight() - (2 * arrowSize);
        int endY = startY + arrowSize;
        return x > startX && x < endX && y > startY && y < endY;
    }

    public boolean isLeftButton(int x, int y) {
        int startX = getWidth() - (3 * arrowSize);
        int endX = startX + arrowSize;
        int startY = getHeight() - (2 * arrowSize);
        int endY = startY + arrowSize;
        return (x > startX) && (x < endX) && (y > startY) && (y < endY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        Rect rect = new Rect(0, 0, getWidth(), getHeight());

        canvas.drawRect(rect, paint);
        drawRoom(canvas);
        drawThings(canvas);
        drawExits(canvas);
        drawPlayers(canvas);
        //drawControls(canvas);
    }

    void drawRoom(Canvas c) {
        if (room != null) {
            if ( backgroundTileSet != null && backgroundTileMap != null ) {
                drawTiles(c, backgroundTileSet, backgroundTileMap);
            }
            if ( foregroundTileSet != null && foregroundTileMap != null ) {
                drawTiles(c, foregroundTileSet, foregroundTileMap);
            }
        }
    }

    private void drawTiles(Canvas canvas, TileSet tileSet, int[][] tileMap) {
        //Draw Tiles
        int rows = room.getRows();
        int cols = room.getCols();
        Paint paint = new Paint();

        int widthPerTile = getWidth() / cols;
        int heightPerTile = getHeight() / rows;
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                int x = c * widthPerTile;
                int y = r * heightPerTile;
                int width = widthPerTile;
                int height = heightPerTile;
                //Check last tiles (row/col)
                if (c == cols) {
                    width = getWidth() - x;
                }
                if ( r == rows ) {
                    height = getHeight() - y;
                }
                if ( tileMap[c][r] >= 0 ) {
                    Bitmap tileImage = tileSet.getTile(tileMap[c][r]);
                    Bitmap drawTile = ImageUtils.resize(tileImage, width, height);
                    canvas.drawBitmap(drawTile, x, y, paint);
                }
            }
        }
    }

    private void drawThings(Canvas c) {
        Paint paint = new Paint();
        for (Thing thing : things) {
            int middleX = thing.getImage().getWidth() / 2;
            int middleY = thing.getImage().getHeight() / 2;
            int x = thing.getX() - middleX;
            int y = thing.getY() - middleY;
            c.drawBitmap(thing.getImage(), x, y, paint);
            if (thing.isSelected()) {
                drawSelectionHighlight(c, thing);
            }
        }
    }

    private void drawExits(Canvas c) {
        Paint paint = new Paint();
        for (Exit exit : exits) {
            int middleX = exit.getImage().getWidth() / 2;
            int middleY = exit.getImage().getHeight() / 2;
            int x = exit.getX() - middleX;
            int y = exit.getY() - middleY;
            c.drawBitmap(exit.getImage(), x, y, paint);
            if (exit.isSelected()) {
                drawSelectionHighlight(c, exit);
            }
        }
    }

    private void drawSpeechBalloon(Canvas canvas, Player p) {
        Paint paint = new Paint();
        int middleX = p.getImage().getWidth() / 2;
        int middleY = p.getImage().getHeight() / 2;
        float speechBalloonScale = (float) middleX / speechBalloon.getWidth() ;
        Bitmap scaledSpeechBalloon = ImageUtils.scaleImage(speechBalloon,speechBalloonScale );
        int x = p.getX() - middleX;
        int y = p.getY() - middleY;
        canvas.drawBitmap( scaledSpeechBalloon,x,y, paint);
    }

    private void drawSelectionHighlight(Canvas canvas, BasicDrawableObject o) {
        int middleX = o.getImage().getWidth() / 2;
        int middleY = o.getImage().getHeight() / 2;
        float thickness = 2f;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(thickness);
        paint.setColor(Color.RED);
        int x = o.getX() - middleX;
        int y = o.getY() - middleY;
        Rect rect = new Rect(x, y, o.getImage().getWidth(), o.getImage().getHeight());
        canvas.drawRect(rect, paint);
    }

    private void drawPlayers(Canvas canvas) {
        Paint paint = new Paint();
        for (Player player : players) {
            Bitmap playerAvatar = player.getImage();
            int middleX = playerAvatar.getWidth() / 2;
            int middleY = playerAvatar.getHeight() / 2;
            int x = player.getX() - middleX;
            int y = player.getY() - middleY;
            canvas.drawBitmap(playerAvatar, x, y, paint);
            Paint.FontMetrics metrics = paint.getFontMetrics();
            int middle = x + (playerAvatar.getWidth() / 2);
            y = y + playerAvatar.getHeight() + 10;
            String displayName = player.getDisplayName();
            if ( currentUserId != null && currentUserId == player.getId()) {
                displayName = "You";
            }
            for (String line : displayName.split(" ")) {
                Rect textBounds = new Rect();
                paint.getTextBounds(line, 0, line.length(), textBounds);
                x = middle - ((int) (paint.measureText(line) / 2));
                paint.setColor(Color.WHITE);
                Rect textBGRect = new Rect(x - 5, y - 10, x+textBounds.width() + 5, y+ textBounds.height() + 2);
                canvas.drawRect(textBGRect, paint);
                paint.setColor(Color.BLACK);
                paint.setTypeface(Typeface.create("Arial",Typeface.BOLD));
                canvas.drawText(line, x, y, paint);
                y += textBounds.height();
            }
            if (player.isSelected()) {
                drawSelectionHighlight(canvas, player);
            }
            if (player.isTalking()) {
                drawSpeechBalloon(canvas, player);
            }
        }
    }

    private int xToScreen(int x) {
        return (int) (unitX * x);
    }

    private int yToScreen(int y) {
        return (int) (unitY * y);
    }


    public void scheduleAction(Action action) {
        synchronized (actions) {
            actions.add(action);
        }
    }

    public void addPlayer(Long userId, String name, byte[] imageData) {
        int roomSize = 10;
        int widthPerTile = getWidth() / roomSize;
        Bitmap image = null;
        Player player = new Player(userId);
        player.setDisplayName(name);
        if (imageData != null) {
            image = ImageUtils.decode(imageData);
            image = ImageUtils.resize(image, widthPerTile, widthPerTile);
        } else {
            image = ImageUtils.resize(defaultUserImage, widthPerTile, widthPerTile);
        }
        player.setImage(image);
        addPlayer(player);
    }


    public void addThing(ThingDto thingDto) {
        Thing thing = new Thing(thingDto.getId());
        thing.setImage(ImageUtils.decode(thingDto.getImageData()));
        thing.setScale(thingDto.getScale());
        thing.setRotation(thingDto.getRotation());
        addThing(thing);
    }

    public void addExit(ExitDto exitDto, String currentServer) {
        Exit exit = new Exit(exitDto.getId(), exitDto.getName());
        if ( exitDto.getToRoomURI() != null && !exitDto.getToRoomURI().equals("") ) {
            exit.setToRoomURI(exitDto.getToRoomURI());
        }
        Bitmap image = ImageUtils.decode(exitDto.getImageData());
        exit.setImage(image);
        exit.setScale(exitDto.getScale());
        exit.setRotation(exitDto.getRotation());
        addExit(exit);
    }

    public void deleteThing(Long thingId) {
        Thing t = getThing(thingId);
        if (t != null) {
            deleteThing(t);
        }
    }

    public void deleteExit(Long exitID) {
        Exit exit = getExit(exitID);
        if (exit != null) {
            deleteExit(exit);
        }
    }

    public void removePlayer(Long playerId) {
        Player player = getPlayer(playerId);
        removePlayer(player);
    }

    public void resetView() {
        //windowController.setRoomname("");
        setForeground(null,null);
        setBackground(null,null);
        setForegroundTileSet(null);
        setBackgroundTileSet(null);
        setRoom(null);
        setPlayers(new ArrayList<>());
        setThings(new ArrayList<>());
        setExits(new ArrayList<>());
    }

    public void setPlayerLocation(Long id, int serverX, int serverY) {
        Player p = getPlayer(id);
        if ( p != null ) {
            setServerLocation(p, serverX, serverY);
        }
    }

    private void setServerLocation(BasicDrawableObject o, int serverX, int serverY) {
        int x = xToScreen(serverX);
        int y = yToScreen(serverY);
        o.setLocation(x, y);
        o.setServerLocation(serverX, serverY);
    }

    public void setThingLocation(Long id, int serverX, int serverY) {
        Thing t = getThing(id);
        if ( t != null ) {
            setServerLocation(t, serverX, serverY);
        };
    }

    public void setExitLocation(Long id, int serverX, int serverY) {
        Exit e = getExit(id);
        if ( e != null ) {
            setServerLocation(e, serverX, serverY);
        }
    }


    public void updatePlayer(UserDto user) {
        Player player = getPlayer(user.getId());
        if (player != null) {
            player.setDisplayName(user.getName());
        }
    }

    public void setAvatar(Long playerId, AvatarDto avatar) {
        Player player = getPlayer(playerId);
        if (player != null) {
            if (avatar != null) {
                Bitmap image = ImageUtils.decode(avatar.getImageData());
                int roomSize = 10;
                int widthPerTile = getWidth() / roomSize;
                image = ImageUtils.resize(image, widthPerTile, widthPerTile);
                player.setImage(image);
            }
        }
    }

    public void updateThing(ThingDto thingDto) {
        Thing thing = getThing(thingDto.getId());
        if (thing != null) {
            thing.setImage(ImageUtils.decode(thingDto.getImageData()));
            thing.setScale(thingDto.getScale());
            thing.setRotation(thingDto.getRotation());
        }
    }

    public void setBackground(TileSet tileSet, int[][] tileMap) {
        this.backgroundTileSet = tileSet;
        this.backgroundTileMap = tileMap;
        if ( tileSet != null) determineUnitXY();
        repaint();
    }

    public void setForeground(TileSet tileSet, int[][] tileMap) {
        this.foregroundTileSet = tileSet;
        this.foregroundTileMap = tileMap;
        repaint();
    }

    public void setBackgroundTileSet(TileSet backgroundTileSet) {
        this.backgroundTileSet = backgroundTileSet;
        repaint();
    }

    public void setForegroundTileSet(TileSet foregroundTileSet) {
        this.foregroundTileSet = foregroundTileSet;
        repaint();
    }

    private void determineUnitXY() {
        int roomWidth = room.getCols() * backgroundTileSet.getTileWidth();
        int roomHeight = room.getRows() * backgroundTileSet.getTileHeight();
        unitX = (float) getWidth() / (float) roomWidth;
        unitY = (float) getHeight() / (float) roomHeight;
        updateAllLocations();
        repaint();
    }

    private void updateAllLocations() {
        for (BasicDrawableObject o : players) {
            setServerLocation(o, o.getServerX(), o.getServerY());
        }
        for (BasicDrawableObject o : things) {
            setServerLocation(o, o.getServerX(), o.getServerY());
        }
        for (BasicDrawableObject o : exits) {
            setServerLocation(o, o.getServerX(), o.getServerY());
        }
    }


    public float getUnitX() {
        return unitX;
    }

    public float getUnitY() {
        return unitY;
    }

}


