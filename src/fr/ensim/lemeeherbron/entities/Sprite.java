package fr.ensim.lemeeherbron.entities;

import fr.ensim.lemeeherbron.terrain.Terrain;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.*;

public class Sprite {

    protected Image image;
    protected double x;
    protected double y;
    protected double width;
    protected double height;
    protected String spriteName;
    private List<Sprite> touchingSprites;

    private double borderX;
    private double borderY;

    private boolean obstacle;

    private boolean portal;

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

        x = 0;
        y = 0;

        this.spriteName = spriteName;

        this.borderX = borderX;
        this.borderY = borderY;

        if(width == 0)
        {
            width = image.getWidth();
        }

        if(height == 0)
        {
            height = image.getHeight();
        }
    }

    public Sprite(String spriteName, double width, double height, double borderX, double borderY)
    {
        this(spriteName, borderX, borderY);

        this.width = width;
        this.height = height;
    }

    public Sprite(String spriteName, double borderX, double borderY, boolean obstacle)
    {
        this(spriteName, borderX, borderY);

        this.obstacle = obstacle;
    }

    public boolean checkMove(Terrain terrain)
    {
        int index = 0;
        boolean found = false;

        if(x + width > borderX || x < 0 || y + height > borderY || y < 0)
        {
            found = true;
        }
        else
        {
            while(index < terrain.getObstacleList().size() && !found)
            {
                if(terrain.getObstacleList().get(index).isObstacle() && intersects(terrain.getObstacleList().get(index)) == 1)
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
        return new Rectangle2D( x, y, 16, 16);
    }

    public int intersects(Sprite spr)
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
                    if(spr.portal)
                    {
                        return 2;
                    }
                    else
                    {
                        touchingSprites.add(spr);

                        return 1;
                    }
                }
            }
        }
        else
        {
            touchingSprites.remove(spr);
        }

        return 0;
    }

    public String getSpriteName()
    {
        return spriteName;
    }

    public void setPortal(boolean portal)
    {
        this.portal = portal;
    }
}
