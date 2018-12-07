package fr.ensim.lemeeherbron;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Terrain {

    private int x;
    private int y;

    private Tile[][] tileTable;
    private List<Sprite> obstacleList;

    public Terrain(int x, int y)
    {
        this.x = x;
        this.y = y;

        tileTable = new Tile[32][32];
        obstacleList = new ArrayList<>();
    }

    public void prepare()
    {
        try {
            File terImg = new File(getClass().getResource("/terrain/" + x + "_" + y + ".png").toURI());
            BufferedImage imgPxls = ImageIO.read(terImg);

            for(int y = 0; y < imgPxls.getHeight(); y++)
            {
                for(int x = 0; x < imgPxls.getWidth(); x++)
                {
                    Tile topTile = null;
                    Tile leftTile = null;
                    Tile cornerTile = null;

                    try {
                        topTile = tileTable[x][y - 1];

                        if(topTile == null)
                        {
                            topTile = new Tile(true);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        topTile = new Tile(true);
                    }

                    try {
                        leftTile = tileTable[x - 1][y];

                        if(leftTile == null)
                        {
                            leftTile = new Tile(true);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        leftTile = new Tile(true);
                    }

                    try {
                        cornerTile = tileTable[x - 1][y - 1];

                        if(cornerTile == null)
                        {
                            cornerTile = new Tile(true);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        cornerTile = new Tile(true);
                    }

                    tileTable[x][y] = getTileForColor(Integer.toHexString(imgPxls.getRGB(x, y)), x * 16, y * 16, topTile, leftTile, cornerTile);

                    Sprite obstacle = getSpriteForColor(Integer.toHexString(imgPxls.getRGB(x, y)));

                    if(obstacle != null)
                    {
                        obstacle.setPosition(x * 16, y * 16);
                        obstacleList.add(obstacle);
                    }
                }
            }

            constructTiles();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void constructTiles()
    {
        for(int i = 0; i < tileTable.length; i++)
        {
            for(int j = 0; j < tileTable[i].length; j++)
            {
                tileTable[i][j].construct();
            }
        }
    }

    public List<Sprite> getObstacleList()
    {
        return obstacleList;
    }

    private Sprite getSpriteForColor(String hexCode)
    {
        Sprite sprite = null;

        switch (hexCode.substring(4, 6))
        {
            case "fe":
                sprite = new Sprite("big_tree", 512, 512, true);
                break;
            case "fd":
                sprite = new Sprite("rock", 512, 512, true);
                break;
            case "f3":
                sprite = new Sprite("building", 512, 512, true);
                break;
        }

        return sprite;
    }

    private Tile getTileForColor(String hexCode, int x, int y, Tile topTile, Tile leftTile, Tile cornerTile)
    {
        Tile tile = null;

        switch (hexCode.substring(6, 8))
        {
            case "ff":
                tile = new Tile("grass", x, y, 3);

                if(topTile.isVariant())
                {
                    topTile.setBl(tile.getTl());
                    topTile.setBr(tile.getTr());
                }

                if(leftTile.isVariant())
                {
                    leftTile.setTr(tile.getTl());
                    leftTile.setBr(tile.getBl());
                }

                if(cornerTile.isVariant())
                {
                    cornerTile.setBr(leftTile.getTr() || topTile.getBl());
                }

                break;
            case "01":
                tile = new Tile("path_dark", x, y, true);

                tile.setTl(leftTile.getTr() && topTile.getBl());
                tile.setTr(topTile.getBr());
                tile.setBl(leftTile.getBr());

                if(!leftTile.isVariant())
                {
                    topTile.setBl(tile.getTl());
                }

                break;
            case "50":
                tile = new Tile("lake", x, y, true);

                tile.setTl(leftTile.getTr());
                tile.setTr(topTile.getBr());
                tile.setBl(leftTile.getBr());

                if(!leftTile.isVariant())
                {
                    topTile.setBl(tile.getTl());
                }
        }

        return tile;
    }

    public void render(GraphicsContext graphicsContext)
    {
        for(int i = 0; i < tileTable.length; i++)
        {
            for(int j = 0; j < tileTable[i].length; j++)
            {
                tileTable[i][j].render(graphicsContext);
            }
        }

        for(Sprite sprite : obstacleList)
        {
            sprite.render(graphicsContext);
        }
    }

    public Tile[][] getTiles()
    {
        return tileTable;
    }
}
