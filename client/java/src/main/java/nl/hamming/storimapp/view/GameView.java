package nl.hamming.storimapp.view;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Timer;

import com.hamming.storim.Controllers;
import com.hamming.storim.model.dto.*;
import com.hamming.storim.util.ImageUtils;
import nl.hamming.storimapp.engine.actions.*;
import nl.hamming.storimapp.engine.actions.Action;

import javax.imageio.ImageIO;
import javax.swing.*;

public class GameView extends JPanel implements Runnable {

    //boolean variable to track if the game is playing or not
    volatile boolean playing;
    private int fps = 30;
    private int arrowSize = 50;
    private int maxUsersX = 10;
    private long time;
    private BufferedImage backBuffer;

    //the game thread
    private Thread gameThread = null;

    private List<Action> actions;
    private List<Player> players;
    private List<Thing> things;
    public RoomDto room;
    public TileDto tile;
    private ViewController viewController;

    private Image defaultTileImage;
    private Image defaultUserImage;
    private Image arrowForward;
    private Image arrowBack;
    private Image arrowLeft;
    private Image arrowRight;
    Timer timer;
    TimerTask task;
    DragTimerTask dragTimerTask;
    private BasicDrawableObject selectedObject;

    private boolean forward, back, left, right;


    private class MyTimerTask extends TimerTask {
        public void run() {
            viewController.sendMoveRequest(forward, back, left, right);
        }
    }

    private class DragTimerTask extends TimerTask {
        private Thing thing;
        private DragTimerTask(Thing thing) {
            this.thing = thing;
        }
        public void run() {
            Point p = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(p, GameView.this);
            thing.setX((int) p.getX());
            thing.setY((int) p.getY() );
            System.out.println(MouseInfo.getPointerInfo().getLocation().x +", "+ MouseInfo.getPointerInfo().getLocation().y);
        }
        public Thing getThing() {
            return thing;
        }
    }


    //Class constructor
    public GameView() {
        actions = Collections.synchronizedList(new LinkedList<Action>());
        players = new ArrayList<>();
        things = new ArrayList<>();
        try {
            defaultTileImage = ImageIO.read(new File("resources/Tile.png"));
            defaultUserImage = ImageIO.read(new File("resources/User.png"));
            arrowForward = ImageIO.read(new File("resources/arrowForward.png"));
            arrowBack = ImageIO.read(new File("resources/arrowBack.png"));
            arrowLeft = ImageIO.read(new File("resources/arrowLeft.png"));
            arrowRight = ImageIO.read(new File("resources/arrowRight.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        timer = new Timer();
        setPreferredSize(new Dimension(500, 500));
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedObject != null) {
                    selectedObject.setSelected(false);
                }
                selectedObject = getSelectedObject(e.getX(), e.getY());
                if (selectedObject != null) {
                    selectedObject.setSelected(true);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (room != null) {
                    forward = isForwardButton(e.getX(), e.getY());
                    back = isBackButton(e.getX(), e.getY());
                    right = isRightButton(e.getX(), e.getY());
                    left = isLeftButton(e.getX(), e.getY());
                    if (forward || back || right || left) {
                        task = new MyTimerTask();
                        timer.scheduleAtFixedRate(task, 0, 50);
                    }
                    BasicDrawableObject b = getSelectedObject(e.getX(), e.getY());
                    if ( b != null && b instanceof Thing) {
                            dragTimerTask = new DragTimerTask((Thing) b);
                            timer.scheduleAtFixedRate(dragTimerTask, 0, 50);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (room != null) {
                    forward = false;
                    back = false;
                    left = false;
                    right = false;
                    if (task != null) task.cancel();
                    if (dragTimerTask != null) {
                        Thing thing = dragTimerTask.getThing();
                        dragTimerTask.cancel();
                        if (thing != null ) {
                            updatePosition(thing);
                        }
                    }
                }
                clearSelection();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    private void updatePosition(Thing thing) {
        viewController.updateThingLocationRequest(thing.getId(), thing.getX(), thing.getY());
    }

    public void setViewController(ViewController viewController) {
        this.viewController = viewController;
    }


    @Override
    public void run() {
        int scaledSize = getWidth() / maxUsersX;
        defaultUserImage = ImageUtils.resize(defaultUserImage, scaledSize, scaledSize);
        while (playing) {
            time = System.currentTimeMillis();
            handleActions();
            draw();
            waitIfNeeded();
        }
    }


    private void handleActions() {
        synchronized (actions) {
            for (Action action : actions) {
                System.out.println("GAMEVIEW ACTION : " + action);
                action.execute();
            }
            actions.removeAll(actions);
        }
    }


    public void scheduleAddThing(ThingDto thing) {
        Action action = new AddThingAction(this, thing);
        synchronized (actions) {
            actions.add(action);
        }
    }


    public void scheduleUpdateThing(ThingDto thing) {
        Action action = new UpdateThingAction(this, thing.getId(), thing.getImage(), thing.getScale(), thing.getRotation());
        synchronized (actions) {
            actions.add(action);
        }
    }

    public void scheduleDeleteThing(ThingDto thing) {
        Action action = new DeleteThingAction(this, thing.getId());
        synchronized (actions) {
            actions.add(action);
        }
    }


    public void scheduleResetView() {
        Action action = new ResetViewAction(this);
        synchronized (actions) {
            actions.add(action);
        }
    }

    public void scheduleDeleteAvatar(Long userId) {
        Action action = new DeleteAvatarAction(this, userId);
        synchronized (actions) {
            actions.add(action);
        }
    }

    public void scheduleUpdateUser(UserDto user, AvatarDto avatar) {
        Action action = new UpdatePlayerAction(this, user, avatar);
        synchronized (actions) {
            actions.add(action);
        }
    }


    public void scheduleAddPlayer(Long userId, String name, Image image) {
        if (image == null) image = defaultUserImage;
        Action action = new AddPlayerAction(this, userId, name, image);
        synchronized (actions) {
            actions.add(action);
        }
    }

    public void addThing(Thing thing) {
        if (!things.contains(thing)) {
            things.add(thing);
        }
    }

    public void deleteThing(Thing thing) {
        if (things.contains(thing)) {
            things.remove(thing);
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

    public void scheduleRemovePlayer(Long userId) {
        Action action = new RemovePlayerAction(this, userId);
        synchronized (actions) {
            actions.add(action);
        }
    }

    public void scheduleSetUserLocation(Long userId, int x, int y) {
        Action action = new SetUserLocationAction(this, userId, x, y);
        synchronized (actions) {
            actions.add(action);
        }
    }

    public RoomDto getRoom() {
        return room;
    }

    public void scheduleSetRoom(RoomDto room) {
        Action action = new SetRoomAction(this, room);
        synchronized (actions) {
            actions.add(action);
        }
    }

    public void scheduleSetTile(TileDto tile) {
        Action action = new SetTileAction(this, tile);
        synchronized (actions) {
            actions.add(action);
        }
    }

    public void setTile(TileDto tile) {
        this.tile = tile;
    }

    public void setRoom(RoomDto room) {
        this.room = room;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }


    public void setThing(ArrayList<Thing> things) {
        this.things = things;
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

    private BasicDrawableObject getSelectedObject(int x, int y) {
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
        return object;
    }

    private void clearSelection() {
        for (BasicDrawableObject thing : things) {
            thing.setSelected(false);
        }
        for (BasicDrawableObject player : players) {
            player.setSelected(false);
        }
        selectedObject = null;
    }

    public void draw() {
        backBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = getGraphics();
        Graphics bbg = backBuffer.getGraphics();

        bbg.setColor(Color.black);
        bbg.fillRect(0, 0, getWidth(), getHeight());
        drawRoom(bbg);
        drawThings(bbg);
        drawUsers(bbg);
        drawControls(bbg);

        g.drawImage(backBuffer, 0, 0, this);
    }

    private void drawControls(Graphics g) {
        g.drawImage(arrowForward, getWidth() - (2 * arrowSize), getHeight() - (3 * arrowSize), arrowSize, arrowSize, this);
        g.drawImage(arrowBack, getWidth() - (2 * arrowSize), getHeight() - (arrowSize), arrowSize, arrowSize, this);
        g.drawImage(arrowLeft, getWidth() - (3 * arrowSize), getHeight() - (2 * arrowSize), arrowSize, arrowSize, this);
        g.drawImage(arrowRight, getWidth() - (arrowSize), getHeight() - (2 * arrowSize), arrowSize, arrowSize, this);
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

    void drawRoom(Graphics g) {
        Image tileImage = defaultTileImage;
        if (room != null) {
            if (tile != null) {
                tileImage = tile.getImage();
            }
            int roomSize = room.getSize();
            int widthPerTile = getWidth() / roomSize;
            int heightPerTile = getHeight() / roomSize;
            for (int i = 0; i < roomSize; i++) {
                for (int j = 0; j < roomSize; j++) {
                    int x = i * widthPerTile;
                    int y = j * heightPerTile;
                    g.drawImage(tileImage, x, y, widthPerTile, heightPerTile, this);
                }
            }

            drawTitle(g);
        }
    }

    private void drawTitle(Graphics g) {
        // Room name
        Font font = new Font("Arial", Font.BOLD, 14);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int textX = (getWidth() / 2) - (metrics.stringWidth(room.getName()) / 2);

        g.setColor(Color.white);
        g.fillRect(textX - 5, 2, metrics.stringWidth(room.getName()) + 5, metrics.getAscent() + 2);
        g.setColor(Color.black);
        g.drawString(room.getName(), textX, metrics.getAscent() + 2);
    }

    private void drawThings(Graphics g) {
        for (Thing thing : things) {
            int middleX = thing.getImage().getWidth(null) / 2;
            int middleY = thing.getImage().getHeight(null) / 2;
            int x = thing.getX() - middleX;
            int y = thing.getY() - middleY;
            g.drawImage(thing.getImage(), x, y, this);
            if (thing.isSelected()) {
                drawSelectionHighlight(g, thing);
            }
        }
    }

    private void drawSelectionHighlight(Graphics g, BasicDrawableObject o) {
        int middleX = o.getImage().getWidth(null) / 2;
        int middleY = o.getImage().getHeight(null) / 2;
        Color old = g.getColor();
        g.setColor(Color.red);
        g.drawRect(o.getX() - middleX, o.getY() - middleY, o.getImage().getWidth(null), o.getImage().getHeight(null));
        g.setColor(old);
    }

    private void drawUsers(Graphics g) {
        int roomSize = 10;
        int widthPerTile = getWidth() / roomSize;
        for (Player player : players) {
            int middleX = player.getImage().getWidth(null) / 2;
            int middleY = player.getImage().getHeight(null) / 2;
            int x = player.getX() - middleX;
            int y = player.getY() - middleY;
            Image playerAvatar = player.getImage();
            g.drawImage(playerAvatar, x, y, this);
            Font font = new Font("Arial", Font.BOLD, 12);
            g.setFont(font);
            FontMetrics metrics = g.getFontMetrics(font);
            int middle = x + (widthPerTile / 2);
            y = y + widthPerTile + 10;
            for (String line : player.getDisplayName().split(" ")) {
                x = middle - (metrics.stringWidth(line) / 2);
                g.setColor(Color.white);
                g.fillRect(x - 5, y - 10, metrics.stringWidth(line) + 5, metrics.getAscent() + 2);
                g.setColor(Color.black);
                g.drawString(line, x, y);
                y += g.getFontMetrics().getHeight();
            }
            if (player.isSelected()) {
                drawSelectionHighlight(g, player);
            }

        }
    }
}


