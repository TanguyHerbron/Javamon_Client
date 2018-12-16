package sample;

import fr.ensim.lemeeherbron.*;
import fr.ensim.lemeeherbron.entities.AnimatedSprite;
import fr.ensim.lemeeherbron.entities.Player;
import fr.ensim.lemeeherbron.entities.Pokemon;
import fr.ensim.lemeeherbron.entities.Sprite;
import fr.ensim.lemeeherbron.terrain.pathfinder.AStarPathFinder;
import fr.ensim.lemeeherbron.terrain.pathfinder.Path;
import fr.ensim.lemeeherbron.terrain.Terrain;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private Canvas mainCanvas;
    @FXML private Label fpsLabel;
    @FXML private ImageView imageSettings;
    @FXML private Pane dialogPane;
    @FXML private Canvas dialogCanvas;
    @FXML private CheckBox checkBoxDrawGrid;
    @FXML private CheckBox checkBoxDrawPath;
    @FXML private CheckBox checkBoxShowFPS;

    private Pokemon leviator;

    private MenuDrawer menuDrawer;

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

    private boolean renderCanvas = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphicsContext = mainCanvas.getGraphicsContext2D();

        menuDrawer = new MenuDrawer(dialogCanvas);
        terrain = new Terrain(0, 0);

        sprites = new ArrayList<>();
        animatedSprites = new ArrayList<>();

        setupPlayer();
        addPikachu();

        //addLokhlass();
        //addPikachu();

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
                            player.setWalking(true);
                        }
                        break;
                    case S:
                        if(direction != 'd')
                        {
                            direction = 'd';
                            numberKeyPressed++;
                            player.setWalking(true);
                        }
                        break;
                    case Q:
                        if(direction != 'l')
                        {
                            direction = 'l';
                            numberKeyPressed++;
                            player.setWalking(true);
                        }
                        break;
                    case D:
                        if(direction != 'r')
                        {
                            direction = 'r';
                            numberKeyPressed++;
                            player.setWalking(true);
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
                    player.setWalking(false);
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

                if(renderCanvas)
                {
                    drawBackground();
                    renderObjects();
                }

                if(checkBoxShowFPS.isSelected())
                {
                    computeFPS(now);
                }
            }
        }.start();

        checkBoxShowFPS.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(checkBoxShowFPS.isSelected())
                {
                    fpsLabel.setVisible(true);
                }
                else
                {
                    fpsLabel.setVisible(false);
                }
            }
        });

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

    private void setupPlayer()
    {
        player = new Player("scientist", 32, 32, 512, 512, 7);
        player.setPosition(mainCanvas.getWidth() / 2, mainCanvas.getHeight() / 2);

        sprites.add(player);
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
        fpsLabel.setVisible(false);
    }

    private void displayAnimationAt(double x, double y)
    {
        AnimatedSprite animatedSprite = new AnimatedSprite("explo", 16, 16, 512, 512, 6, x, y);
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
        Pokemon lokSprite = new Pokemon("lokhlass", 32, 32, 512, 512, 3, true);
        lokSprite.setPosition(mainCanvas.getWidth() / 4, mainCanvas.getHeight() / 4);

        sprites.add(lokSprite);
    }

    private void addPikachu()
    {
        pikaSprite = new Pokemon("leviator", 32, 32, 512, 512, 10, true);
        pikaSprite.setPosition(380, 380);

        sprites.add(pikaSprite);
    }

    private void movePokemons()
    {
        /*for(Sprite sprite : sprites)
        {
            Pokemon pokemon = (Pokemon) sprite;

            if(pokemon.hasBehavior())
            {
                Entity entities = (Entity) sprite;

                if(((Pokemon) entities).hasBehavior())
                {
                    ((Pokemon) entities).move(terrain);
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

        if(checkBoxDrawGrid.isSelected())
        {
            terrain.drawGrid(graphicsContext);
        }

        if(checkBoxDrawPath.isSelected() && path != null)
        {
            path.render(graphicsContext);
        }

        /*if(selectedSprite != null)
        {
            selectedSprite.render(graphicsContext);
        }*/

        if(dialogPane.isVisible())
        {
            menuDrawer.draw();
        }
    }

    private void renderObjects()
    {
        for(Sprite sprite : sprites) {
            sprite.render(graphicsContext);
        }

        for(Sprite obs : terrain.getObstacleList())
        {
            if(player.intersects(obs) == 2)
            {
                player.setPosition(-1, -1);

                switch (terrain.getValue())
                {
                    case "00":
                        fadeOutTransition();
                        terrain = new Terrain("inside1");
                        player.setPosition(240, 288);
                        break;
                    case "inside1":
                        fadeOutTransition();
                        terrain = new Terrain(0, 0);
                        player.setPosition(304, 256);
                        break;
                }

                sprites.clear();
                sprites.add(player);
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

    private void fadeOutTransition()
    {
        renderCanvas = false;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mainCanvas.getOpacity() > 0)
                {
                    mainCanvas.setOpacity(mainCanvas.getOpacity() - 0.1);

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                while(!terrain.isReady());

                fadeInTransition();
            }
        }).start();
    }

    private void fadeInTransition()
    {
        renderCanvas = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mainCanvas.getOpacity() < 1)
                {
                    mainCanvas.setOpacity(mainCanvas.getOpacity() + 0.1);

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}