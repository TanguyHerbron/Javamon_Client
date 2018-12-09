package sample;

import com.sun.javafx.geom.Vec2f;
import fr.ensim.lemeeherbron.*;
import fr.ensim.lemeeherbron.pathfinder.AStarPathFinder;
import fr.ensim.lemeeherbron.pathfinder.Path;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.scene.input.KeyCode.*;

public class Controller implements Initializable {

    @FXML private Canvas mainCanvas;
    @FXML private Label fpsLabel;
    private Player player;
    private Terrain terrain;
    private GraphicsContext graphicsContext;

    private List<AnimatedSprite> animatedSprites;

    private List<Sprite> sprites;
    private List<Image> backImage;

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

        backImage = new ArrayList<>();

        for(int i = 0; i < 4; i++)
        {
            backImage.add(new Image("/tile/grass_" + i + ".png"));
        }

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
                //displayAnimation();

                int x = (int) Math.round(event.getX() / 16);
                int y = (int) Math.round(event.getY() / 16);

                int xp = (int) Math.round(pikaSprite.getX() / 16);
                int xy = (int) Math.round(pikaSprite.getY() / 16);

                selectedSprite = new Sprite("selected", 512, 512);
                selectedSprite.setPosition(x * 16, y * 16);

                System.out.println("Loading path from " + xp * 16 + " " + xy * 16 + " to " + event.getX() + " " + event.getY());

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
    }

    private void displayAnimation(double x, double y)
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
        pikaSprite = new Pokemon("pikachu", 512, 512, 2, true);
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

        if(path != null)
        {
            path.render(graphicsContext);
        }

        if(selectedSprite != null)
        {
            selectedSprite.render(graphicsContext);
        }
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