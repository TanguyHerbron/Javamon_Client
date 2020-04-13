package fr.ensim.lemeeherbron.entities;

import fr.ensim.lemeeherbron.terrain.Terrain;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

public class Entity extends Sprite {

    protected double speed;

    protected char lastMove;

    private static int spriteCounter = 0;

    public Entity() {}

    public Entity(String spriteName, double width, double height, double borderX, double borderY, double speed) {
        super(spriteName, width, height, borderX, borderY);

        this.speed = speed;
    }

    public Entity(String spriteName, double x, double y, char orientation)
    {
        super(spriteName, 32, 32, 512, 512);

        this.x = x;
        this.y = y;
        this.lastMove = orientation;
    }

    public void up(Terrain terrain)
    {
        y -= speed;

        lastMove = 'u';

        if(checkMove(terrain))
        {
            y += speed;
        }
    }

    public void down(Terrain terrain)
    {
        y += speed;

        lastMove = 'd';

        if(checkMove(terrain))
        {
            y -= speed;
        }
    }

    public void left(Terrain terrain)
    {
        x -= speed;

        lastMove = 'l';

        if(checkMove(terrain))
        {
            x += speed;
        }
    }

    public void right(Terrain terrain)
    {
        x += speed;

        lastMove = 'r';

        if(checkMove(terrain))
        {
            x -= speed;
        }
    }

    public void reverseMove()
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

    public char getOrientation()
    {
        return lastMove;
    }

    @Override
    public Rectangle2D getBoundary()
    {
        return new Rectangle2D(x + (width - 16 - (width - 16) / 2), y + (height - 16), 16, 16);
    }

    public static void updateSprites()
    {
        spriteCounter++;
        spriteCounter %= 31;
    }

    @Override
    public void render(GraphicsContext graphicsContext)
    {
        int xPiece = 0;
        int yPiece = 0;

        switch (lastMove)
        {
            case 'u':
                if(spriteCounter > 15)
                {
                    xPiece = 0;
                    yPiece = 0;
                }
                else
                {
                    xPiece = 0;
                    yPiece = 32;
                }
                break;
            case 'd':
                if(spriteCounter > 15)
                {
                    xPiece = 0;
                    yPiece = 64;
                }
                else
                {
                    xPiece = 0;
                    yPiece = 96;
                }
                break;
            case 'l':
                if(spriteCounter > 15)
                {
                    xPiece = 32;
                    yPiece = 0;
                }
                else
                {
                    xPiece = 32;
                    yPiece = 32;
                }
                break;
            case 'r':
                if(spriteCounter > 15)
                {
                    xPiece = 32;
                    yPiece = 64;
                }
                else
                {
                    xPiece = 32;
                    yPiece = 96;
                }
                break;
        }

        graphicsContext.drawImage(image, xPiece, yPiece, width, height, x, y, width, height);
    }
}
