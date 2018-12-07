package fr.ensim.lemeeherbron;

import com.sun.javafx.geom.Vec2f;

public class Pokemon extends Entity {

    private boolean behavior;
    private Vec2f target;

    public Pokemon(String spriteName, double borderX, double borderY, int speed, boolean behavior) {
        super(spriteName, borderX, borderY, speed);

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
        if(target != null && (target.x != 0 || target.y != 0))
        {
            if(target.x != 0)
            {
                if(target.x > 0)
                {
                    left(terrain);
                    target.x -= speed;
                }
                else
                {
                    right(terrain);
                    target.x += speed;
                }
            }
            else
            {
                if(target.y > 0)
                {
                    up(terrain);
                    target.y -= speed;
                }
                else
                {
                    down(terrain);
                    target.y += speed;
                }
            }
        }
        else
        {
            generateTarget();
        }
    }
}
