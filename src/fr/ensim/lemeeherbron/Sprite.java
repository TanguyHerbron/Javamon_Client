package fr.ensim.lemeeherbron;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.nio.Buffer;
import java.util.*;

public class Sprite {

    private Image image;
    private double x;
    private double y;
    private double width;
    private double height;
    private int speed = 5;
    private String spriteName;
    private List<Sprite> touchingSprites;
    private Sprite pet;

    private boolean obstable;

    private char lastMove;

    public Sprite(String spriteName)
    {
        touchingSprites = new ArrayList<>();

        try {
            image = new Image("/sprite/" + spriteName + "_f.png");
        } catch (IllegalArgumentException e) {
            System.out.println("Loading default sprite texture");

            image = new Image("/sprite/" + spriteName + ".png");
        }

        width = image.getWidth();
        height = image.getHeight();

        x = 0;
        y = 0;

        this.spriteName = spriteName;
    }

    public Sprite(String spriteName, boolean obstable)
    {
        this(spriteName);

        this.obstable = obstable;
    }

    public void givePet()
    {
        pet = new Sprite("pikachu");
        pet.setPosition(x, y);
    }

    public void up()
    {
        y -= speed;
        image = new Image("/sprite/" + spriteName + "_b.png");

        lastMove = 'u';
    }

    public void down()
    {
        y += speed;
        image = new Image("/sprite/" + spriteName + "_f.png");

        lastMove = 'd';
    }

    public void left()
    {
        x -= speed;
        image = new Image("/sprite/" + spriteName + "_l.png");

        lastMove = 'l';
    }

    public void right()
    {
        x += speed;
        image = new Image("/sprite/" + spriteName + "_r.png");

        lastMove = 'r';
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
        return new Rectangle2D(x, y, width, height);
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

                    System.out.println("I'm touching");

                    return true;
                }
            }
        }
        else
        {
            if(touchingSprites.remove(spr))
            {
                System.out.println("I stopped touching");
            }
        }

        return false;
    }
}
