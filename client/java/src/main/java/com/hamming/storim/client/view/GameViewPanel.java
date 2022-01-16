package com.hamming.storim.client.view;


import com.hamming.storim.client.ImageUtils;
import com.hamming.storim.common.controllers.ViewController;
import com.hamming.storim.common.dto.*;
import com.hamming.storim.common.view.Action;
import com.hamming.storim.common.view.GameView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.*;

public class GameViewPanel extends JPanel implements GameView, Runnable {

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
    private List<Exit> exits;
    public RoomDto room;
    public TileDto tile;
    public Image tileImage;
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
    private float unitX = 1f;
    private float unitY = 1f;



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
            SwingUtilities.convertPointFromScreen(p, GameViewPanel.this);
            thing.setX((int) p.getX());
            thing.setY((int) p.getY());
        }
        public Thing getThing() {
            return thing;
        }
    }


    //Class constructor
    public GameViewPanel() {
        actions = Collections.synchronizedList(new LinkedList<Action>());
        players = new ArrayList<>();
        things = new ArrayList<>();
        exits = new ArrayList<>();
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
                clearSelection();
                selectedObject = getSelectedObject(e.getX(), e.getY());
                if (selectedObject != null) {

                    if (selectedObject instanceof Exit) {
                        viewController.setSelectedExit(selectedObject.getId());
                    } else {
                        selectedObject.setSelected(!selectedObject.isSelected()); //Flip
                        if (selectedObject.isSelected()) {
                            selectObject(selectedObject);
                        }
                    }
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
                    if (b != null && b instanceof Thing) {
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
                        if (thing != null) {
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

    public void setViewController(ViewController viewController) {
        this.viewController = viewController;
    }

    public Image getDefaultUserImage() {
        return defaultUserImage;
    }

    private void selectObject(BasicDrawableObject selectedObject) {
        if ( selectedObject instanceof  Thing ) {
            viewController.setSelectedThing(selectedObject.getId());
        } else if (selectedObject instanceof Player) {
            viewController.setSelectedPlayer(selectedObject.getId());
        }
    }

    private void updatePosition(Thing thing) {
        viewController.updateThingLocationRequest(thing.getId(), thing.getX(), thing.getY());
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
                action.execute();
            }
            actions.removeAll(actions);
        }
    }

    public void addThing(Thing thing) {
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

    public RoomDto getRoom() {
        return room;
    }

    @Override
    public void setTile(TileDto tile) {
        this.tile = tile;
    }

    @Override
    public void setRoom(RoomDto room) {
        this.room = room;
        if (room != null) {
            unitX = getWidth() / room.getWidth();
            unitY = getHeight() / room.getLength();
        }
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
        selectedObject = null;
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

    public void draw() {
        backBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = getGraphics();
        Graphics bbg = backBuffer.getGraphics();

        bbg.setColor(Color.black);
        bbg.fillRect(0, 0, getWidth(), getHeight());
        drawRoom(bbg);
        drawThings(bbg);
        drawExits(bbg);
        drawUsers(bbg);
        drawControls(bbg);

        g.drawImage(backBuffer, 0, 0, this);
    }

    void drawRoom(Graphics g) {
        tileImage = defaultTileImage;
        if (room != null) {
            if (tile != null) {
                tileImage = ImageUtils.decode(tile.getImageData());
            }
            //Draw Tiles
            int rows = room.getRows();
            int cols = room.getCols();

            int widthPerTile = getWidth() / cols;
            int heightPerTile = getHeight() / rows;
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
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
            int x = (int)(thing.getX() * unitX) - middleX;
            int y = (int)(thing.getY() * unitY) - middleY;
            g.drawImage(thing.getImage(), x, y, this);
            if (thing.isSelected()) {
                drawSelectionHighlight(g, thing);
            }
        }
    }

    private void drawExits(Graphics g) {
        for (Exit exit : exits) {
            int w = exit.getImage().getWidth(null);
            int h = exit.getImage().getHeight(null);
            g.drawImage(exit.getImage(), exit.getX() - (w/2), exit.getY()-(h/2), this);
            if (exit.isSelected()) {
                drawSelectionHighlight(g, exit);
            }
        }
    }

    private void drawSelectionHighlight(Graphics g, BasicDrawableObject o) {
        int middleX = o.getImage().getWidth(null) / 2;
        int middleY = o.getImage().getHeight(null) / 2;
        float thickness = 4f;
        Graphics2D g2 = (Graphics2D) g;
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(thickness));
        Color old = g.getColor();
        g.setColor(Color.red);
        g.drawRect((int)(o.getX() ) - middleX, (int)(o.getY()) - middleY, o.getImage().getWidth(null), o.getImage().getHeight(null));
        g.setColor(old);
        g2.setStroke(oldStroke);
    }

    private void drawUsers(Graphics g) {
        for (Player player : players) {
            int middleX = player.getImage().getWidth(null) / 2;
            int middleY = player.getImage().getHeight(null) / 2;
            int x = (int)(player.getX() * unitX) - middleX;
            int y = (int)(player.getY() * unitY ) - middleY;
            Image playerAvatar = player.getImage();
            g.drawImage(playerAvatar, x, y,  this);
            Font font = new Font("Arial", Font.BOLD, 12);
            g.setFont(font);
            FontMetrics metrics = g.getFontMetrics(font);
            int middle = x + (player.getImage().getWidth(null) / 2);
            y = y + player.getImage().getHeight(null) + 10;
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


    @Override
    public void scheduleAction(Action action) {
        synchronized (actions) {
            actions.add(action);
        }
    }

    @Override
    public void addPlayer(Long userId, String name, byte[] imageData) {
        int roomSize = 10;
        int widthPerTile = getWidth() / roomSize;
        Image image = null;
        Player player = new Player(userId);
        player.setDisplayName(name);
        if (imageData != null ) {
            image = ImageUtils.decode(imageData);
            image = ImageUtils.resize(image, widthPerTile, widthPerTile);
        } else {
            image = ImageUtils.resize(defaultUserImage, widthPerTile, widthPerTile);
        }
        player.setImage(image);
        addPlayer(player);
    }



    @Override
    public void addThing(ThingDto thingDto) {
        Thing thing = new Thing(thingDto.getId());
        thing.setImage(ImageUtils.decode(thingDto.getImageData()));
        thing.setScale(thingDto.getScale());
        thing.setRotation(thingDto.getRotation());
        thing.setX(thingDto.getLocation().getX());
        thing.setY(thingDto.getLocation().getY());
        addThing(thing);
    }

    @Override
    public void addExit(ExitDto exitDto) {
        //FIXME Multiple exits with the same orientation will be drawn at the same place...
        Exit exit = new Exit(exitDto.getId(), exitDto.getName(), Exit.Orientation.valueOf(exitDto.getOrientation().name()));
        Image image = exit.getImage();
        int x = 0;
        int y = 0;
        switch (exit.getOrientation() ) {
            case NORTH:
                // Draw on top
                x = getWidth() / 2;
                y = image.getHeight(null) / 2;
                break;
            case SOUTH:
                // Draw on bottom
                x = getWidth() / 2;
                y = getHeight() - image.getHeight(null) / 2;
                break;
            case EAST:
                // Draw right
                x = getWidth() - (image.getWidth(null) / 2 );
                y = getHeight() / 2;
                break;
            case WEST:
                //draw left
                x = image.getWidth(null) / 2;
                y = getHeight() / 2;
                break;
        }
        exit.setX(x);
        exit.setY(y);
        addExit(exit);
    }

    @Override
    public void deleteAvatar(Long playerId) {
        Player player = getPlayer(playerId);
        if (player != null ) {
            player.setImage(null);
        }
    }

    @Override
    public void deleteThing(Long thingId) {
        Thing t = getThing(thingId);
        if ( t != null ) {
            deleteThing(t);
        }
    }

    @Override
    public void removePlayer(Long playerId) {
        Player player = getPlayer(playerId);
        removePlayer(player);
    }

    @Override
    public void resetView() {
        setTile(null);
        setRoom(null);
        setPlayers(new ArrayList<>());
        setThings(new ArrayList<>());
        setExits(new ArrayList<>());
    }

    @Override
    public void setPlayerLocation(Long playerId, int x, int y) {
        Player p = getPlayer(playerId);
        //FIXME : p should not be null!
        if ( p != null ) {
            p.setX(x);
            p.setY(y);
        }
    }

    @Override
    public void updatePlayer(UserDto user, AvatarDto avatar) {
        Player player = getPlayer(user.getId());
        if (player != null ) {
            player.setDisplayName(user.getName());
            if ( avatar != null ) {
                Image image = ImageUtils.decode(avatar.getImageData());
                int roomSize = 10;
                int widthPerTile = getWidth() / roomSize;
                image = ImageUtils.resize(image, widthPerTile, widthPerTile);
                player.setImage(image);
            } else {
                player.setImage(getDefaultUserImage());
            }
        }
    }

    @Override
    public void updateThing(ThingDto thingDto) {
        Thing thing = getThing(thingDto.getId());
        if ( thing != null ) {
            thing.setImage(ImageUtils.decode(thingDto.getImageData()));
            thing.setScale(thingDto.getScale());
            thing.setRotation(thingDto.getRotation());
            thing.setX(thingDto.getLocation().getX());
            thing.setY(thingDto.getLocation().getY());
        }
    }
}

