package sample;

import fr.ensim.lemeeherbron.*;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.scene.input.KeyCode.*;

public class Controller implements Initializable {

    @FXML private Canvas mainCanvas;
    private Player player;
    private Terrain terrain;
    private GraphicsContext graphicsContext;

    private List<AnimatedSprite> animatedSprites;

    private List<Sprite> sprites;
    private List<Image> backImage;

    private char direction = '0';

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphicsContext = mainCanvas.getGraphicsContext2D();
        player = new Player("player", 512, 512, 2);

        terrain = new Terrain(0, 1);
        terrain.prepare();

        sprites = new ArrayList<>();
        animatedSprites = new ArrayList<>();

        player.setPosition(mainCanvas.getWidth() / 2, mainCanvas.getHeight() / 2);

        addLokhlass();
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
                        direction = 'u';
                        break;
                    case S:
                        direction = 'd';
                        break;
                    case Q:
                        direction = 'l';
                        break;
                    case D:
                        direction = 'r';
                        break;
                }
            }
        });

        mainCanvas.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == Z || event.getCode() == S ||event.getCode() == Q ||event.getCode() == D)
                {
                    direction = '0';
                }
            }
        });

        mainCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                AnimatedSprite animatedSprite = new AnimatedSprite("explo", 512, 512, 6, event.getX(), event.getY());
                animatedSprite.start();

                animatedSprites.add(animatedSprite);
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

    private void addLokhlass()
    {
        Pokemon lokSprite = new Pokemon("lokhlass", 512, 512, 3, true);
        lokSprite.setPosition(mainCanvas.getWidth() / 4, mainCanvas.getHeight() / 4);

        sprites.add(lokSprite);
    }

    private void addPikachu()
    {
        Pokemon pikaSprite = new Pokemon("pikachu", 512, 512, 5, true);
        pikaSprite.setPosition(mainCanvas.getWidth() / 1.5, mainCanvas.getHeight() / 1.5);

        sprites.add(pikaSprite);
    }

    private void movePokemons()
    {
        for(Sprite sprite : sprites)
        {
            if(sprite instanceof Pokemon)
            {
                Entity entity = (Entity) sprite;

                if(((Pokemon) entity).hasBehavior())
                {
                    ((Pokemon) entity).move(terrain);
                }
            }
        }
    }

    private void drawBackground()
    {
        terrain.render(graphicsContext);
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