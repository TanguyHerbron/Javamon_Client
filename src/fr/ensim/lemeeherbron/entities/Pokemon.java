package fr.ensim.lemeeherbron.entities;

import com.sun.javafx.geom.Vec2f;
import fr.ensim.lemeeherbron.terrain.Terrain;

public class Pokemon extends Entity {

    private boolean behavior;
    private Vec2f target;

    public Pokemon(String spriteName, int width, int height, double borderX, double borderY, int speed, boolean behavior) {
        super(spriteName, width, height, borderX, borderY, speed);

        this.behavior = behavior;
    }

    public void setBehavior(boolean behavior)
    {
        this.behavior = behavior;
    }

    public boolean hasBehavior()
    {
        return behavior;
    }

    private void generateTarget()
    {
        int chance = (int) (Math.round(Math.random() * 50));

        if(chance == 1)
        {
            target = new Vec2f();

            target.x = ((int) Math.round((Math.random() * 10) - 5)) * speed;
            target.y = ((int) Math.round((Math.random() * 10) - 5)) * speed;
        }
    }

    public void move(Terrain terrain)
    {
        double x = Math.round(this.x);
        double y = Math.round(this.y);

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

            if(Math.abs(target.x - this.x) < speed)
            {
                this.x = target.x;
            }

            if(Math.abs(target.y - this.y) < speed)
            {
                this.y = target.y;
            }

            if(this.x == target.x && this.y == target.y)
            {
                target = null;
            }
        }
    }

    public void setTarget(int x, int y)
    {
        target = new Vec2f(x, y);
    }

    public int getSpeed()
    {
        return speed;
    }

    public boolean hasTarget()
    {
        return target != null;
    }
}