package sample;

import fr.ensim.lemeeherbron.Pokemon;
import fr.ensim.lemeeherbron.Sprite;
import fr.ensim.lemeeherbron.Terrain;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private Canvas mainCanvas;
    private Sprite playerSprite;
    private Terrain terrain;
    private GraphicsContext graphicsContext;

    private List<Sprite> sprites;
    private List<Image> backImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphicsContext = mainCanvas.getGraphicsContext2D();
        playerSprite = new Sprite("player");
        Pokemon lokSprite = new Pokemon("lokhlass", true);
        Pokemon pikaSprite = new Pokemon("pikachu", true);
        terrain = new Terrain(0, 0);
        terrain.prepare();

        sprites = new ArrayList<>();

        playerSprite.setPosition(mainCanvas.getWidth() / 2, mainCanvas.getHeight() / 2);
        lokSprite.setPosition(mainCanvas.getWidth() / 4, mainCanvas.getHeight() / 4);
        pikaSprite.setPosition(mainCanvas.getWidth() / 1.5, mainCanvas.getHeight() / 1.5);

        sprites.add(pikaSprite);
        sprites.add(lokSprite);
        sprites.add(playerSprite);

        backImage = new ArrayList<>();

        for(int i = 0; i < 4; i++)
        {
            backImage.add(new Image("/sprite/grass_" + i + ".png"));
        }

        mainCanvas.setFocusTraversable(true);

        mainCanvas.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode())
                {
                    case Z:
                        playerSprite.up(terrain);
                        break;
                    case S:
                        playerSprite.down(terrain);
                        break;
                    case Q:
                        playerSprite.left(terrain);
                        break;
                    case D:
                        playerSprite.right(terrain);
                        break;
                }
            }
        });

        new AnimationTimer() {
            @Override
            public void handle(long now) {
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
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void movePokemons()
    {
        Sprite sprite = sprites.get((int) Math.round(Math.random() * (sprites.size() - 1)));

        if(sprite instanceof Pokemon)
        {
            Pokemon pokemon = (Pokemon) sprite;

            if(pokemon.hasBehavior())
            {
                switch ((int) Math.round(Math.random() * 3))
                {
                    case 0:
                        pokemon.up(terrain);
                        break;
                    case 1:
                        pokemon.down(terrain);
                        break;
                    case 2:
                        pokemon.left(terrain);
                        break;
                    default:
                        pokemon.right(terrain);
                        break;
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
        }

        for(Sprite sprite : terrain.getObstacleList())
        {
            playerSprite.intersects(sprite);
        }
    }
}