package com.hamming.storim.client.view;


import com.hamming.storim.client.ImageUtils;
import com.hamming.storim.client.STORIMWindowController;
import com.hamming.storim.client.controller.GameViewController;
import com.hamming.storim.common.dto.*;
import com.hamming.storim.common.view.Action;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.*;

public class GameViewPanel extends JPanel implements Runnable {

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
    private int[][] backgroundTileMap;
    private int[][] foregroundTileMap;

    private TileSet backgroundTileSet;
    private TileSet foregroundTileSet;
    private GameViewController viewController;
    private Image defaultUserImage;
    private Image arrowForward;
    private Image arrowBack;
    private Image arrowLeft;
    private Image arrowRight;
    private BufferedImage speechBalloon;
    private static int SPEECH_BALLOON_TIME = 2000;
    Timer timer;
    TimerTask task;
    DragThingTimerTask dragThingTimerTask;
    DragExitTimerTask dragExitTimerTask;
    private BasicDrawableObject selectedObject;
    private boolean forward, back, left, right;
    private float unitX = 1f;
    private float unitY = 1f;
    private STORIMWindowController windowController;
    private Long currentUserId;

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

    public void setCurrentUserId(Long id) {
        currentUserId = id;
    }


    private class MyTimerTask extends TimerTask {
        public void run() {
            viewController.sendMoveRequest(forward, back, left, right);
        }
    }

    private class DragThingTimerTask extends TimerTask {
        private Thing thing;

        private DragThingTimerTask(Thing thing) {
            this.thing = thing;
        }

        public void run() {
            Point p = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(p, GameViewPanel.this);
            int newX = (int) p.getX();
            int newY = (int) p.getY();
            thing.setLocation(newX, newY);
            repaint();
        }

        public Thing getThing() {
            return thing;
        }
    }

    private class DragExitTimerTask extends TimerTask {
        private Exit exit;

        private DragExitTimerTask(Exit exit) {
            this.exit = exit;
        }

        public void run() {
            Point p = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(p, GameViewPanel.this);
            int newX = (int) p.getX();
            int newY = (int) p.getY();
            exit.setLocation(newX, newY);
            repaint();
        }

        public Exit getExit() {
            return exit;
        }
    }


    //Class constructor
    public GameViewPanel(STORIMWindowController windowController) {
        this.windowController = windowController;
        actions = Collections.synchronizedList(new LinkedList<Action>());
        players = new ArrayList<>();
        things = new ArrayList<>();
        exits = new ArrayList<>();
        try {
            defaultUserImage = ImageIO.read(new File("resources/User.png"));
            arrowForward = ImageIO.read(new File("resources/arrowForward.png"));
            arrowBack = ImageIO.read(new File("resources/arrowBack.png"));
            arrowLeft = ImageIO.read(new File("resources/arrowLeft.png"));
            arrowRight = ImageIO.read(new File("resources/arrowRight.png"));
            speechBalloon = ImageIO.read(new File("resources/speechballoon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        timer = new Timer();
        setPreferredSize(new Dimension(500, 500));
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clearSelection();
                Point p = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(p, GameViewPanel.this);
                selectedObject = getSelectedObject(p.x, p.y);
                if (selectedObject != null) {
                    selectedObject.setSelected(!selectedObject.isSelected()); //Flip
                    if (selectedObject.isSelected()) {
                        selectObject(selectedObject);
                        repaint();
                    }
                    if (selectedObject instanceof Exit) {
                        Exit exit = (Exit) selectedObject;
                        viewController.exitClicked(exit.getId(), exit.getName(), exit.getToRoomURI());
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
                    if (b != null && e.isControlDown())
                        if (b instanceof Thing) {
                            dragThingTimerTask = new DragThingTimerTask((Thing) b);
                            timer.scheduleAtFixedRate(dragThingTimerTask, 0, 50);
                        } else if (b instanceof Exit) {
                            dragExitTimerTask = new DragExitTimerTask((Exit) b);
                            timer.scheduleAtFixedRate(dragExitTimerTask, 0, 50);
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
                    if (dragThingTimerTask != null) {
                        Thing thing = dragThingTimerTask.getThing();
                        dragThingTimerTask.cancel();
                        if (thing != null) {
                            updatePosition(thing);
                        }
                        dragThingTimerTask = null;
                    } else if (dragExitTimerTask != null) {
                        Exit exit = dragExitTimerTask.getExit();
                        dragExitTimerTask.cancel();
                        if (exit != null) {
                            updatePosition(exit);
                        }
                        dragExitTimerTask = null;
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

        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                if ( room != null && backgroundTileSet != null ) determineUnitXY();
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });
    }

    public void setViewController(GameViewController viewController) {
        this.viewController = viewController;
    }

    private void selectObject(BasicDrawableObject selectedObject) {
        if (selectedObject instanceof Thing) {
            //TODO
        } else if (selectedObject instanceof Player) {
            //TODO
        } else if (selectedObject instanceof Exit) {
            //TODO
        }
    }

    private void updatePosition(Thing thing) {
        int translatedX = (int) (thing.getX() / unitX);
        int translatedY = (int) (thing.getY() / unitY);
        viewController.updateThingLocationRequest(thing.getId(), translatedX, translatedY);
        repaint();
    }

    private void updatePosition(Exit exit) {
        int translatedX = (int) (exit.getX() / unitX);
        int translatedY = (int) (exit.getY() / unitY);
        viewController.updateExitLocationRequest(exit.getId(), translatedX, translatedY);
        repaint();
    }


    @Override
    public void run() {
        int scaledSize = getWidth() / maxUsersX;
        defaultUserImage = ImageUtils.resize(defaultUserImage, scaledSize, scaledSize);
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
        if (room != null) {
            String text = room.getRoomURI() + " (" + room.getName() + ")";
            windowController.setRoomname(text);
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
        for (BasicDrawableObject exit : exits) {
            exit.setSelected(false);
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

    @Override
    public void paint(Graphics g) {
        backBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics bbg = backBuffer.getGraphics();

        bbg.setColor(Color.black);
        bbg.fillRect(0, 0, getWidth(), getHeight());
        drawRoom(bbg);
        drawThings(bbg);
        drawExits(bbg);
        drawPlayers(bbg);
        drawControls(bbg);

        g.drawImage(backBuffer, 0, 0, this);
    }

    void drawRoom(Graphics g) {
        if (room != null) {
            if ( backgroundTileSet != null && backgroundTileMap != null ) {
                drawTiles(g, backgroundTileSet, backgroundTileMap);
            }
            if ( foregroundTileSet != null && foregroundTileMap != null ) {
                drawTiles(g, foregroundTileSet, foregroundTileMap);
            }
        }
    }

    private void drawTiles(Graphics g, TileSet tileSet, int[][] tileMap) {
        //Draw Tiles
        int rows = room.getRows();
        int cols = room.getCols();

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
                    Image tileImage = tileSet.getTile(tileMap[c][r]);
                    g.drawImage(tileImage, x, y, width, height, this);
                }
            }
        }
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

    private void drawExits(Graphics g) {
        for (Exit exit : exits) {
            int middleX = exit.getImage().getWidth(null) / 2;
            int middleY = exit.getImage().getHeight(null) / 2;
            int x = exit.getX() - middleX;
            int y = exit.getY() - middleY;
            g.drawImage(exit.getImage(), x, y, this);
            if (exit.isSelected()) {
                drawSelectionHighlight(g, exit);
            }
        }
    }

    private void drawSpeechBalloon(Graphics g, Player p) {
        int middleX = p.getImage().getWidth(null) / 2;
        int middleY = p.getImage().getHeight(null) / 2;
        float speechBalloonScale = (float) middleX / speechBalloon.getWidth(null) ;
        Image scaledSpeechBalloon = ImageUtils.scaleImage(speechBalloon,speechBalloonScale );
        int x = p.getX() - middleX;
        int y = p.getY() - middleY;
        g.drawImage( scaledSpeechBalloon,x,y, null);
    }

    private void drawSelectionHighlight(Graphics g, BasicDrawableObject o) {
        int middleX = o.getImage().getWidth(null) / 2;
        int middleY = o.getImage().getHeight(null) / 2;
        float thickness = 2f;
        Graphics2D g2 = (Graphics2D) g;
        Stroke oldStroke = g2.getStroke();
        Stroke dashed = new BasicStroke(thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[]{3,2}, 0);
        g2.setStroke(dashed);
        Color old = g.getColor();
        g.setColor(Color.red);
        int x = o.getX() - middleX;
        int y = o.getY() - middleY;
        g.drawRect(x, y, o.getImage().getWidth(null), o.getImage().getHeight(null));
        g.setColor(old);
        g2.setStroke(oldStroke);
    }

    private void drawPlayers(Graphics g) {
        for (Player player : players) {
            Image playerAvatar = player.getImage();
            int middleX = playerAvatar.getWidth(null) / 2;
            int middleY = playerAvatar.getHeight(null) / 2;
            int x = player.getX() - middleX;
            int y = player.getY() - middleY;
            g.drawImage(playerAvatar, x, y, this);
            Font font = new Font("Arial", Font.BOLD, 12);
            g.setFont(font);
            FontMetrics metrics = g.getFontMetrics(font);
            int middle = x + (playerAvatar.getWidth(null) / 2);
            y = y + playerAvatar.getHeight(null) + 10;
            String displayName = player.getDisplayName();
            if ( currentUserId != null && currentUserId == player.getId()) {
                displayName = "You";
            }
            for (String line : displayName.split(" ")) {
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
            if (player.isTalking()) {
                drawSpeechBalloon(g, player);
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
        Image image = null;
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
        Image image = ImageUtils.decode(exitDto.getImageData());
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
        windowController.setRoomname("");
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
                Image image = ImageUtils.decode(avatar.getImageData());
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


