package fr.ensim.lemeeherbron;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.*;

public class Sprite {

    private Image image;
    private double x;
    private double y;
    private double width;
    private double height;
    protected int speed = 5;
    private String spriteName;
    private List<Sprite> touchingSprites;
    private Sprite pet;

    private double borderX;
    private double borderY;

    private boolean obstable;

    private char lastMove;

    public Sprite(String spriteName, double borderX, double borderY)
    {
        touchingSprites = new ArrayList<>();

        try {
            image = new Image("/sprite/" + spriteName + "_f.png");
        } catch (IllegalArgumentException e) {
            image = new Image("/sprite/" + spriteName + ".png");
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

        this.obstable = obstable;
    }

    public void givePet()
    {
        pet = new Sprite("pikachu", borderX, borderY);
        pet.setPosition(x, y);
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
                if(intersects(terrain.getObstacleList().get(index)))
                {
                    found = true;
                }

                index++;
            }
        }

        return found;
    }

    public void up(Terrain terrain)
    {
        y -= speed;
        image = new Image("/sprite/" + spriteName + "_b.png");

        lastMove = 'u';

        if(checkMove(terrain))
        {
            y += speed;
        }
    }

    public void down(Terrain terrain)
    {
        y += speed;
        image = new Image("/sprite/" + spriteName + "_f.png");

        lastMove = 'd';

        if(checkMove(terrain))
        {
            y -= speed;
        }
    }

    public void left(Terrain terrain)
    {
        x -= speed;
        image = new Image("/sprite/" + spriteName + "_l.png");

        lastMove = 'l';

        if(checkMove(terrain))
        {
            x += speed;
        }
    }

    public void right(Terrain terrain)
    {
        x += speed;
        image = new Image("/sprite/" + spriteName + "_r.png");

        lastMove = 'r';

        if(checkMove(terrain))
        {
            x -= speed;
        }
    }

    public void setPosition(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public boolean isObstable()
    {
        return obstable;
    }

    private void reverseMove()
    {
        switch (lastMove)
        {
            case 'u':
                y += speed;
                break;
            case 'd':
                y -= speed;
                break;
            case 'l':
                x += speed;
                break;
            case 'r':
                x -= speed;
                break;
        }
    }

    public void render(GraphicsContext graphicsContext)
    {
        graphicsContext.drawImage(image, x, y);

        if(pet != null)
        {
            pet.render(graphicsContext);
        }
    }

    public Rectangle2D getBoundary()
    {
        return new Rectangle2D(x, y, width - 4, height - 4);
    }

    public synchronized boolean intersects(Sprite spr)
    {
        if(spr.getBoundary().intersects(this.getBoundary()))
        {
            if(!touchingSprites.contains(spr))
            {
                if(spr.isObstable())
                {
                    reverseMove();
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
