package sample;

import fr.ensim.lemeeherbron.*;
import fr.ensim.lemeeherbron.pathfinder.AStarPathFinder;
import fr.ensim.lemeeherbron.pathfinder.Path;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private Canvas mainCanvas;
    @FXML private Label fpsLabel;
    @FXML private ImageView imageSettings;
    @FXML private Pane dialogPane;
    @FXML private Canvas dialogCanvas;

    private Player player;
    private Terrain terrain;
    private GraphicsContext graphicsContext;

    private List<AnimatedSprite> animatedSprites;

    private List<Sprite> sprites;

    //PATHFINDING
    private Pokemon pikaSprite;
    private Sprite selectedSprite;
    private Path path;

    private char direction = '0';
    private int numberKeyPressed;

    //FPS counter variables
    private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0;
    private boolean arrayFilled = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphicsContext = mainCanvas.getGraphicsContext2D();
        player = new Player("player", 512, 512, 2);

        terrain = new Terrain(0, 0);
        terrain.prepare();

        sprites = new ArrayList<>();
        animatedSprites = new ArrayList<>();

        player.setPosition(mainCanvas.getWidth() / 2, mainCanvas.getHeight() / 2);

        //addLokhlass();
        addPikachu();

        sprites.add(player);

        mainCanvas.setFocusTraversable(true);

        mainCanvas.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode())
                {
                    case Z:
                        if(direction != 'u')
                        {
                            direction = 'u';
                            numberKeyPressed++;
                        }
                        break;
                    case S:
                        if(direction != 'd')
                        {
                            direction = 'd';
                            numberKeyPressed++;
                        }
                        break;
                    case Q:
                        if(direction != 'l')
                        {
                            direction = 'l';
                            numberKeyPressed++;
                        }
                        break;
                    case D:
                        if(direction != 'r')
                        {
                            direction = 'r';
                            numberKeyPressed++;
                        }
                        break;
                }
            }
        });

        //TODO Redo the entire input system (maybe add threads and mutexs to avoid multiple input glitchs ?)
        mainCanvas.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                if(event.getCode().equals(KeyCode.Z) || event.getCode().equals(KeyCode.Q) || event.getCode().equals(KeyCode.S) || event.getCode().equals(KeyCode.D))
                {
                    numberKeyPressed--;
                }

                if(numberKeyPressed == 0)
                {
                    direction = '0';
                }
            }
        });

        mainCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                int x = (int) Math.round(event.getX() / 16);
                int y = (int) Math.round(event.getY() / 16);

                int xp = (int) Math.round(pikaSprite.getX() / 16);
                int xy = (int) Math.round(pikaSprite.getY() / 16);

                selectedSprite = new Sprite("selected", 512, 512);
                selectedSprite.setPosition(x * 16, y * 16);

                AStarPathFinder aStarPathFinder = new AStarPathFinder(terrain, 100);

                path = aStarPathFinder.findPath(xp, xy, x, y);
            }
        });

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                switch (direction)
                {
                    case 'u':
                        player.up(terrain);
                        break;
                    case 'd':
                        player.down(terrain);
                        break;
                    case 'l':
                        player.left(terrain);
                        break;
                    case 'r':
                        player.right(terrain);
                        break;
                }

                drawBackground();
                renderObjects();

                computeFPS(now);
            }
        }.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    movePokemons();

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        setupSettingsButton();
    }

    private void setupSettingsButton()
    {
        imageSettings.setImage(new Image("/menu/gear.png"));
        imageSettings.setFitHeight(24);
        imageSettings.setFitWidth(24);

        imageSettings.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                dialogPane.setVisible(!dialogPane.isVisible());
            }
        });

        dialogPane.setVisible(false);
    }

    private void displayAnimationAt(double x, double y)
    {
        AnimatedSprite animatedSprite = new AnimatedSprite("explo", 512, 512, 6, x, y);
        animatedSprite.start();

        animatedSprites.add(animatedSprite);
    }

    private void computeFPS(long now)
    {
        long oldFrameTime = frameTimes[frameTimeIndex];
        frameTimes[frameTimeIndex] = now;
        frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
        if(frameTimeIndex == 0)
        {
            arrayFilled = true;
        }

        if(arrayFilled)
        {
            long elapsedNanos = now - oldFrameTime;
            long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
            double frameRate = 1_000_000_000.0 / elapsedNanosPerFrame;
            fpsLabel.setText(String.format("%.2f FPS", frameRate));
        }

    }

    private void addLokhlass()
    {
        Pokemon lokSprite = new Pokemon("lokhlass", 512, 512, 3, true);
        lokSprite.setPosition(mainCanvas.getWidth() / 4, mainCanvas.getHeight() / 4);

        sprites.add(lokSprite);
    }

    private void addPikachu()
    {
        pikaSprite = new Pokemon("pikachu", 512, 512, 10, true);
        pikaSprite.setPosition(380, 380);

        sprites.add(pikaSprite);
    }

    private void movePokemons()
    {
        /*for(Sprite sprite : sprites)
        {
            if(sprite instanceof Pokemon)
            {
                Entity entity = (Entity) sprite;

                if(((Pokemon) entity).hasBehavior())
                {
                    ((Pokemon) entity).move(terrain);
                }
            }
        }*/

        if(path != null)
        {
            if(pikaSprite.getX() == path.getFirstStep().getX() && pikaSprite.getY() == path.getFirstStep().getY())
            {
                path.completeFistStep();

                if(path.getLength() == 0)
                {
                    path = null;
                }
            }

            if(!pikaSprite.hasTarget() && path != null)
            {
                pikaSprite.setTarget(path.getFirstStep().getX(), path.getFirstStep().getY());
            }

            pikaSprite.move(terrain);
        }
    }

    private void drawBackground()
    {
        terrain.render(graphicsContext);
        //terrain.drawGrid(graphicsContext);

        if(path != null)
        {
            path.render(graphicsContext);
        }

        /*if(selectedSprite != null)
        {
            selectedSprite.render(graphicsContext);
        }*/

        if(dialogPane.isVisible())
        {
            drawMenuBackground((int) Math.round(dialogCanvas.getWidth()), (int) Math.round(dialogCanvas.getHeight()));

            drawMenuLine((int) Math.round(dialogCanvas.getWidth()), 0, "top");
            drawMenuLine((int) Math.round(dialogCanvas.getWidth()), (int) Math.round(dialogCanvas.getHeight()) - 8, "bot");
            drawMenuLine((int) Math.round(dialogCanvas.getHeight()), 0, "left");
            drawMenuLine((int) Math.round(dialogCanvas.getHeight()), (int) Math.round(dialogCanvas.getWidth()) - 8, "right");

            drawMenuAngle(0, 0, 0);
            drawMenuAngle((int) Math.round(dialogCanvas.getWidth() - 8), 0, 90);
            drawMenuAngle(0, (int) Math.round(dialogCanvas.getHeight() - 8), 270);
            drawMenuAngle((int) Math.round(dialogCanvas.getWidth() - 8), (int) Math.round(dialogCanvas.getHeight() - 8), 180);
        }
    }

    private void drawMenuBackground(int width, int height)
    {
        Image background = new Image("/menu/background.png");

        for(int y = 8; y < height - 8; y += background.getHeight())
        {
            for(int x = 8; x < width - 8; x += background.getWidth())
            {
                dialogCanvas.getGraphicsContext2D().drawImage(background, x, y);
            }
        }
    }

    private void drawMenuLine(int size, int offset, String side)
    {
        Image line = new Image("/menu/line.png");

        ImageView iv = new ImageView(line);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        switch (side)
        {
            case "top":
                iv.setRotate(0);

                line = iv.snapshot(params, null);

                for(int i = 8; i < size - 8; i+=8)
                {
                    dialogCanvas.getGraphicsContext2D().drawImage(line, i, offset);
                }
                break;
            case "bot":
                iv.setRotate(180);

                line = iv.snapshot(params, null);

                for(int i = 8; i < size - 8; i+=8)
                {
                    dialogCanvas.getGraphicsContext2D().drawImage(line, i, offset);
                }
                break;
            case "left":
                iv.setRotate(270);

                line = iv.snapshot(params, null);

                for(int i = 8; i < size - 8; i+=8)
                {
                    dialogCanvas.getGraphicsContext2D().drawImage(line, offset, i);
                }
                break;
            case "right":
                iv.setRotate(90);

                line = iv.snapshot(params, null);

                for(int i = 8; i < size - 8; i+=8)
                {
                    dialogCanvas.getGraphicsContext2D().drawImage(line, offset, i);
                }
                break;
        }
    }

    private void drawMenuAngle(int x, int y, int rotation)
    {
        Image corner = new Image("/menu/corner.png");

        ImageView iv = new ImageView(corner);
        iv.setRotate(rotation);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        corner = iv.snapshot(params, null);

        dialogCanvas.getGraphicsContext2D().drawImage(corner, x, y);
    }

    private void renderObjects()
    {
        for(Sprite sprite : sprites)
        {
            sprite.render(graphicsContext);

            for(Sprite obs : terrain.getObstacleList())
            {
                sprite.intersects(obs);
            }
        }

        int index = 0;

        while(index < animatedSprites.size())
        {
            if(animatedSprites.get(index).getAnimationState() == 0)
            {
                animatedSprites.remove(animatedSprites.get(index));
            }
            else
            {
                animatedSprites.get(index).render(graphicsContext);
            }

            index++;
        }
    }
}