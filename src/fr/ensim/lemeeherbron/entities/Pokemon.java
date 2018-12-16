package fr.ensim.lemeeherbron.entities;

import com.sun.javafx.geom.Vec2f;
import fr.ensim.lemeeherbron.terrain.Terrain;

public class Pokemon extends Entity {

    private boolean behavior;
    private Vec2f target;

    public Pokemon(String spriteName, int width, int height, double borderX, double borderY, int speed, boolean behavior) {
        super("pokemon/" + spriteName, width, height, borderX, borderY, speed);

        this.behavior = behavior;
    }

    public void setPosX(double x)
    {
        this.x = x - 8;
    }

    public void setPosY(double y)
    {
        this.y = y - 16;
    }

    @Override
    public void setPosition(double x, double y)
    {
        setPosX(x);
        setPosY(y);
    }

    @Override
    public double getX()
    {
        return getBoundary().getMinX();
    }

    @Override
    public double getY()
    {
        return getBoundary().getMinY();
    }

    public void setBehavior(boolean behavior)
    {
        this.behavior = behavior;
    }

    public boolean hasBehavior()
    {
        return behavior;
    }

    public void move(Terrain terrain)
    {
        double x = Math.round(getBoundary().getMinX());
        double y = Math.round(getBoundary().getMinY());

        if(target != null && (target.x != x || target.y != y))
        {
            if(target.x != x)
            {
                if(target.x < x)
                {
                    left(terrain);
                }
                else
                {
                    right(terrain);
                }
            }
            else
            {
                if(target.y < y)
                {
                    up(terrain);
                }
                else
                {
                    down(terrain);
                }
            }

            if(Math.abs(target.x - getX()) < speed)
            {
                setPosX(target.x);
            }

            if(Math.abs(target.y - getY()) < speed)
            {
                setPosY(target.y);
            }

            if(getX() == target.x && getY() == target.y)
            {
                target = null;
            }
        }
    }

    public void setTarget(int x, int y)
    {
        target = new Vec2f(x, y);
    }

    public double getSpeed()
    {
        return speed;
    }

    public boolean hasTarget()
    {
        return target != null;
    }
}
