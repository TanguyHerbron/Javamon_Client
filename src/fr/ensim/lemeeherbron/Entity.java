package fr.ensim.lemeeherbron;

import com.sun.javafx.geom.Vec2d;
import com.sun.javafx.geom.Vec2f;
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
}
