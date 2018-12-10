package fr.ensim.lemeeherbron;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.*;

public class Sprite {

    protected Image image;
    protected double x;
    protected double y;
    private double width;
    private double height;
    protected String spriteName;
    private List<Sprite> touchingSprites;

    private double borderX;
    private double borderY;

    private boolean obstacle;

    public Sprite(String spriteName, double borderX, double borderY)
    {
        touchingSprites = new ArrayList<>();

        if(spriteName.equals(""))
        {
            image = new Image("/sprite/blank.png");;
        }
        else
        {
            try {
                image = new Image("/sprite/" + spriteName + "_f.png");
            } catch (IllegalArgumentException e) {
                image = new Image("/sprite/" + spriteName + ".png");
            }
        }

        this.width = image.getWidth();
        this.height = image.getHeight();

        x = 0;
        y = 0;

        this.spriteName = spriteName;

        this.borderX = borderX;
        this.borderY = borderY;
    }

    public Sprite(String spriteName, double borderX, double borderY, boolean obstable)
    {
        this(spriteName, borderX, borderY);

        this.obstacle = obstable;
    }

    public boolean checkMove(Terrain terrain)
    {
        int index = 0;
        boolean found = false;

        if(x + 32 > borderX || x < 0 || y + 32 > borderY || y < 0)
        {
            found = true;
        }
        else
        {
            while(index < terrain.getObstacleList().size() && !found)
            {
                if(intersects(terrain.getObstacleList().get(index)))
                {
                    found = true;
                }

                index++;
            }
        }

        return found;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public void setPosition(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public boolean isObstacle()
    {
        return obstacle;
    }

    public void render(GraphicsContext graphicsContext)
    {
        graphicsContext.drawImage(image, x, y);
    }

    public Rectangle2D getBoundary()
    {
        return new Rectangle2D(x, y, width - 8, height - 8);
    }

    public synchronized boolean intersects(Sprite spr)
    {
        if(spr.getBoundary().intersects(this.getBoundary()))
        {
            if(!touchingSprites.contains(spr))
            {
                if(spr.isObstacle())
                {
                    if(this instanceof Entity)
                    {
                        ((Entity) this).reverseMove();
                    }
                }
                else
                {
                    touchingSprites.add(spr);

                    return true;
                }
            }
        }
        else
        {
            touchingSprites.remove(spr);
        }

        return false;
    }
}
