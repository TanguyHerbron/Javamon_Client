package fr.ensim.lemeeherbron;

import com.sun.javafx.geom.Vec2d;
import com.sun.javafx.geom.Vec2f;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Entity extends Sprite {

    protected int speed;

    private char lastMove;

    public Entity(String spriteName, double borderX, double boderY, int speed) {
        super(spriteName, borderX, boderY);

        this.speed = speed;
    }

    public void up(Terrain terrain)
    {
        y -= speed;
        //image = new Image("/sprite/" + spriteName + "_b.png");

        lastMove = 'u';

        if(checkMove(terrain))
        {
            y += speed;
        }
    }

    public void down(Terrain terrain)
    {
        y += speed;
        //image = new Image("/sprite/" + spriteName + "_f.png");

        lastMove = 'd';

        if(checkMove(terrain))
        {
            y -= speed;
        }
    }

    public void left(Terrain terrain)
    {
        x -= speed;
        //image = new Image("/sprite/" + spriteName + "_l.png");

        lastMove = 'l';

        if(checkMove(terrain))
        {
            x += speed;
        }
    }

    public void right(Terrain terrain)
    {
        x += speed;
        //image = new Image("/sprite/" + spriteName + "_r.png");

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

        graphicsContext.drawImage(image, xPiece, yPiece, 32, 32, x, y, 32, 32);
    }
}
