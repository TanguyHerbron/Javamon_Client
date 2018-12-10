package fr.ensim.lemeeherbron.entities;

import fr.ensim.lemeeherbron.terrain.Terrain;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public class Entity extends Sprite {

    protected double speed;

    protected char lastMove;

    public Entity(String spriteName, double width, double height, double borderX, double borderY, double speed) {
        super(spriteName, width, height, borderX, borderY);

        this.speed = speed;
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

    @Override
    public Rectangle2D getBoundary()
    {
        return new Rectangle2D(x, y, 16, 16);
    }

    @Override
    public void render(GraphicsContext graphicsContext)
    {
        int xPiece = 0;
        int yPiece = 0;

        switch (lastMove)
        {
            case 'u':
                xPiece = 0;
                yPiece = 0;
                break;
            case 'd':
                xPiece = 0;
                yPiece = 64;
                break;
            case 'l':
                xPiece = 32;
                yPiece = 0;
                break;
            case 'r':
                xPiece = 32;
                yPiece = 64;
                break;
        }

        graphicsContext.drawImage(image, xPiece, yPiece, width, height, x, y, width, height);
    }
}
