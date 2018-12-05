package fr.ensim.lemeeherbron;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Terrain {

    private int x;
    private int y;

    private Image[][] textureList;
    private List<Sprite> obstacleList;

    public Terrain(int x, int y)
    {
        this.x = x;
        this.y = y;

        textureList = new Image[32][32];
        obstacleList = new ArrayList<>();
    }

    public void prepare()
    {
        try {
            File terImg = new File(getClass().getResource("/terrain/" + x + "_" + y + ".png").toURI());
            BufferedImage imgPxls = ImageIO.read(terImg);

            for(int i = 0; i < imgPxls.getHeight(); i++)
            {
                for(int j = 0; j < imgPxls.getWidth(); j++)
                {
                    textureList[i][j] = getTileForColor(Integer.toHexString(imgPxls.getRGB(i, j)));

                    Sprite obstacle = getSpriteForColor(Integer.toHexString(imgPxls.getRGB(i, j)));

                    if(obstacle != null)
                    {
                        obstacle.setPosition(i * 16, j * 16);
                        obstacleList.add(obstacle);
                    }
                }
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public List<Sprite> getObstacleList()
    {
        return obstacleList;
    }

    private Sprite getSpriteForColor(String hexCode)
    {
        Sprite sprite = null;

        System.out.println(hexCode);

        switch (hexCode.substring(4, 6))
        {
            case "fe":
                sprite = new Sprite("big_tree", true);
                break;
            case "fd":
                sprite = new Sprite("rock", true);
                break;
            case "fc":
                sprite = new Sprite("lake_center", true);
                break;
            case "fb":
                sprite = new Sprite("lake_right", true);
                break;
            case "fa":
                sprite = new Sprite("lake_left", true);
                break;
            case "f9":
                sprite = new Sprite("lake_top", true);
                break;
            case "f8":
                sprite = new Sprite("lake_bot", true);
                break;
            case "f7":
                sprite = new Sprite("lake_bot_right", true);
                break;
            case "f6":
                sprite = new Sprite("lake_bot_left", true);
                break;
            case "f5":
                sprite = new Sprite("lake_top_right", true);
                break;
            case "f4":
                sprite = new Sprite("lake_top_left", true);
                break;
        }

        return sprite;
    }

    private Image getTileForColor(String hexCode)
    {
        Image image = null;

        switch (hexCode.substring(6, 8))
        {
            case "ff":
                image = new Image("/sprite/grass_3.png");
                break;
            case "02":
                image = new Image("/sprite/path_dark_up.png");
                break;
            case "01":
                image = new Image("/sprite/path_dark.png");
                break;
            case "03":
                image = new Image("/sprite/path_dark_down.png");
                break;
            case "04":
                image = new Image("/sprite/path_dark_turn_up_right.png");
                break;
            case "05":
                image = new Image("/sprite/path_dark_turn_up_left.png");
                break;
            case "06":
                image = new Image("/sprite/path_dark_right.png");
                break;
            case "07":
                image = new Image("/sprite/path_dark_left.png");
                break;
            case "08":
                image = new Image("/sprite/path_dark_up_right.png");
                break;
            case "09":
                image = new Image("/sprite/path_dark_up_left.png");
                break;
        }

        return image;
    }

    public void render(GraphicsContext graphicsContext)
    {
        for(int i = 0; i < textureList.length; i++)
        {
            for(int j = 0; j < textureList[i].length; j++)
            {
                graphicsContext.drawImage(textureList[i][j], i * 16, j * 16);
            }
        }

        for(Sprite sprite : obstacleList)
        {
            sprite.render(graphicsContext);
        }
    }
}
