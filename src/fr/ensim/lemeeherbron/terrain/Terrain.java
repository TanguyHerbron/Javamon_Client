package fr.ensim.lemeeherbron.terrain;

import fr.ensim.lemeeherbron.entities.Sprite;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Terrain {

    private Tile[][] tileTable;
    private List<Sprite> obstacleList;
    private String value;

    private boolean ready;

    private static Terrain INSTANCE;

    public static Terrain getInstance()
    {
        return INSTANCE;
    }

    public static Terrain build(String name)
    {
        INSTANCE = new Terrain(name);

        return INSTANCE;
    }

    public static Terrain build(int x, int y)
    {
        INSTANCE = new Terrain(x, y);

        return INSTANCE;
    }

    private Terrain(int x, int y)
    {
        tileTable = new Tile[32][32];
        obstacleList = new ArrayList<>();

        this.value = x + "" + y;

        prepare(x, y);
    }

    private Terrain(String insideName)
    {
        tileTable = new Tile[32][32];
        obstacleList = new ArrayList<>();

        this.value = insideName;

        prepare(insideName);
    }

    private void prepare(String insideName)
    {
        try {
            File terImg = new File(getClass().getResource("/terrain/inside/" + insideName + ".png").toURI());
            generateTerrain(ImageIO.read(terImg));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void prepare(int x, int y)
    {
        try {
            File terImg = new File(getClass().getResource("/terrain/" + x + "_" + y + ".png").toURI());
            generateTerrain(ImageIO.read(terImg));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void generateTerrain(BufferedImage imgPxls)
    {
        boolean noObs = false;
        ready = false;

        for(int yG = 0; yG < imgPxls.getHeight(); yG++) {
            for (int xG = 0; xG < imgPxls.getWidth(); xG++) {
                Tile topTile = null;
                Tile leftTile = null;
                Tile cornerTile = null;

                try {
                    topTile = tileTable[xG][yG - 1];

                    if (topTile == null) {
                        topTile = new Tile(true);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    topTile = new Tile(true);
                }

                try {
                    leftTile = tileTable[xG - 1][yG];

                    if (leftTile == null) {
                        leftTile = new Tile(true);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    leftTile = new Tile(true);
                }

                try {
                    cornerTile = tileTable[xG - 1][yG - 1];

                    if (cornerTile == null) {
                        cornerTile = new Tile(true);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    cornerTile = new Tile(true);
                }

                tileTable[xG][yG] = getTileForColor(Integer.toHexString(imgPxls.getRGB(xG, yG)), xG * 16, yG * 16, topTile, leftTile, cornerTile);

                Sprite obstacle = null;

                if(noObs)
                {
                    noObs = false;
                }
                else
                {
                    obstacle = getSpriteForColor(Integer.toHexString(imgPxls.getRGB(xG, yG)));
                }

                if (obstacle != null) {
                    obstacle.setPosition(xG * 16, yG * 16);
                    obstacleList.add(obstacle);

                    if(obstacle.getSpriteName() == "carpet")
                    {
                        noObs = true;
                    }
                }
            }
        }

        constructTiles();
    }


    private void constructTiles()
    {
        for(int i = 0; i < tileTable.length; i++)
        {
            for(int j = 0; j < tileTable[i].length; j++)
            {
                if(tileTable[i][j] != null)
                {
                    tileTable[i][j].construct();
                }
            }
        }

        ready = true;
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
            case "ff":
                sprite = new Sprite("big_tree", 512, 512, true);
                break;
            case "fa":
                sprite = new Sprite("", 512, 512, true);
                break;
            case "f5":
                sprite = new Sprite("rock", 512, 512, true);
                break;
            case "f0":
                sprite = new Sprite("building", 512, 512, true);
                break;
            case "e8":
                sprite = new Sprite("", 512, 512);
                sprite.setPortal(true);
                break;
            case "e5":
                sprite = new Sprite("carpet", 512, 512, false);
                break;
            case "e2":
                sprite = new Sprite("bar", 512, 512, false);
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
            case "00":
                tile = new Tile("black", x, y, false);

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
            case "50":
                tile = new Tile("lake", x, y, true);

                tile.setTl(leftTile.getTr());
                tile.setTr(topTile.getBr());
                tile.setBl(leftTile.getBr());

                if(!leftTile.isVariant())
                {
                    topTile.setBl(tile.getTl());
                }
                break;
            case "60":
                tile = new Tile("floor", x, y, true);

                tile.setTl(leftTile.getTr());
                tile.setTr(topTile.getBr());
                tile.setBl(leftTile.getBr());

                if(!leftTile.isVariant())
                {
                    topTile.setBl(tile.getTl());
                }
                break;
        }

        return tile;
    }

    public void render(GraphicsContext graphicsContext)
    {
        for(int i = 0; i < tileTable.length; i++)
        {
            for(int j = 0; j < tileTable[i].length; j++)
            {
                if(tileTable[i][j] != null)
                {
                    tileTable[i][j].render(graphicsContext);
                }
            }
        }
    }

    public boolean blocked(int x, int y)
    {
        for(int i = 0; i < obstacleList.size(); i++)
        {
            if(Math.round(obstacleList.get(i).getX() / 16) == x && Math.round(obstacleList.get(i).getY() / 16) == y)
            {
                return true;
            }
        }

        return false;
    }

    public int getWidth()
    {
        return tileTable.length;
    }

    public int getHeight()
    {
        return tileTable[0].length;
    }

    public Tile[][] getTiles()
    {
        return tileTable;
    }

    public void drawGrid(GraphicsContext graphicsContext)
    {
        graphicsContext.setLineWidth(1.0);
        graphicsContext.setStroke(Color.WHITE);
        graphicsContext.setGlobalAlpha(0.4);

        for(int i = 0; i < tileTable.length; i++)
        {
            graphicsContext.strokeLine(i * 16, 0, i * 16, tileTable.length * 16);
        }

        for(int i = 0; i < tileTable[0].length; i++)
        {
            graphicsContext.strokeLine(0, i * 16, tileTable[0].length * 16, i * 16);
        }

        graphicsContext.setGlobalAlpha(1);
    }

    public String getValue()
    {
        return value;
    }

    public boolean isReady()
    {
        return ready;
    }
}
