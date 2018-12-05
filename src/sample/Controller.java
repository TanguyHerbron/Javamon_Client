package sample;

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
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private Canvas mainCanvas;
    private Sprite playerSprite;
    private Sprite lokSprite;
    private Terrain terrain;
    private GraphicsContext graphicsContext;

    private List<Sprite> sprites;
    private List<Image> backImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphicsContext = mainCanvas.getGraphicsContext2D();
        playerSprite = new Sprite("player");
        lokSprite = new Sprite("lokhlass");
        terrain = new Terrain(0, 0);

        sprites = new ArrayList<>();

        playerSprite.setPosition(mainCanvas.getWidth() / 2, mainCanvas.getHeight() / 2);
        lokSprite.setPosition(mainCanvas.getWidth() / 4, mainCanvas.getHeight() / 4);

        terrain.prepare();

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
                        playerSprite.up();
                        break;
                    case S:
                        playerSprite.down();
                        break;
                    case Q:
                        playerSprite.left();
                        break;
                    case D:
                        playerSprite.right();
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