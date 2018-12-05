package sample;

import com.sun.javafx.geom.Vec2d;
import fr.ensim.lemeeherbron.Sprite;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private Canvas mainCanvas;
    private Sprite playerSprite;
    private Sprite pikaSprite;
    private GraphicsContext graphicsContext;
    private int[][] backGrid;

    private List<Sprite> sprites;
    private List<Image> backImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphicsContext = mainCanvas.getGraphicsContext2D();
        playerSprite = new Sprite("player");
        pikaSprite = new Sprite("pikachu");

        sprites = new ArrayList<>();

        playerSprite.setPosition(mainCanvas.getWidth() / 2, mainCanvas.getHeight() / 2);
        pikaSprite.setPosition(mainCanvas.getWidth() / 4, mainCanvas.getHeight() / 4);

        sprites.add(pikaSprite);
        sprites.add(playerSprite);

        generateEnvironment();

        backImage = new ArrayList<>();

        for(int i = 0; i < 4; i++)
        {
            backImage.add(new Image("/sprite/grass_" + i + ".png"));
        }

        backGrid = new int[32][32];

        for(int i = 0; i < backGrid.length; i++)
        {
            for(int j = 0; j < backGrid[i].length; j++)
            {
                backGrid[i][j] = (int) Math.round(Math.random() * 3);
            }
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

    private void generateEnvironment()
    {
        for(int i = 0; i < 10; i++)
        {
            Sprite sprite = new Sprite("big_tree", true);
            sprite.setPosition(Math.round(Math.random() * 500), Math.round(Math.random() * 500));

            sprites.add(sprite);
        }
    }

    private void drawBackground()
    {
        Image backImg = backImage.get(backGrid[0][0]);

        for(int i = 0; backImg.getHeight() * i < mainCanvas.getHeight(); i++)
        {
            for(int j = 0; backImg.getHeight() * j < mainCanvas.getWidth(); j++)
            {
                graphicsContext.drawImage(backImg, backImg.getWidth() * i, backImg.getHeight() * j);

                backImg = backImage.get(backGrid[i][j]);
            }
        }
    }

    private void renderObjects()
    {
        for(Sprite sprite : sprites)
        {
            sprite.render(graphicsContext);
        }

        for(int i = 0; i < sprites.size(); i++)
        {
            for(int j = i + 1; j < sprites.size(); j++)
            {
                if(sprites.get(i).intersects(sprites.get(j)))
                {
                    Sprite pi = new Sprite("pikachu");
                    pi.setPosition(Math.round(Math.random() * 500), Math.round(Math.random() * 500));

                    sprites.add(pi);
                }
            }
        }
    }
}