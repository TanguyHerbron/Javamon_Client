package fr.ensim.lemeeherbron.entities;

import com.sun.javafx.geom.Vec2f;
import fr.ensim.lemeeherbron.terrain.Terrain;
import fr.ensim.lemeeherbron.terrain.pathfinder.AStarPathFinder;
import fr.ensim.lemeeherbron.terrain.pathfinder.Path;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Pokemon extends Entity {

    private static final AtomicInteger count = new AtomicInteger(0);
    private int id;
    private boolean behavior;
    private Vec2f target;
    private Path path;
    private Terrain terrain;

    private int tempoMove;

    public Pokemon(String spriteName, int width, int height, double borderX, double borderY, int speed, boolean behavior) {
        super("pokemon/" + spriteName, width, height, borderX, borderY, speed);

        this.behavior = behavior;
        id = count.incrementAndGet();
    }

    public Pokemon(String spriteName, double borderX, double borderY, int speed, boolean behavior) {
        this(spriteName, 32, 32, borderX, borderY, speed, behavior);
    }

    public Pokemon(String spriteName, double borderX, double borderY, int speed, boolean behavior, Terrain terrain) {
        this(spriteName, 32, 32, borderX, borderY, speed, behavior, terrain);
    }

    public Pokemon(String spriteName, int width, int height, double borderX, double borderY, int speed, boolean behavior, Terrain terrain)
    {
        this(spriteName, width, height, borderX, borderY, speed, behavior);

        this.terrain = terrain;
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

    public void simulateBehavior()
    {
        if(path != null)
        {
            if(tempoMove == 500)
            {
                move();
                tempoMove = 0;
            }
            else
            {
                tempoMove++;
            }
        }
        else
        {
            if(hasBehavior())
            {
                boolean wantsToMove = new Random().nextInt(5)==0;

                if(wantsToMove)
                {
                    Random rand = new Random();

                    int xp = (int) Math.floor(getX() / 16);
                    int xy = (int) Math.floor(getY() / 16);

                    AStarPathFinder aStarPathFinder = new AStarPathFinder(terrain, 100);

                    path = aStarPathFinder.findPath(xp, xy, rand.nextInt(32), rand.nextInt(32));

                    tempoMove = 0;
                }
            }
        }
    }

    public void move()
    {
        if(path != null)
        {
            if(getX() == path.getFirstStep().getX() && getY() == path.getFirstStep().getY())
            {
                path.completeFistStep();

                if(path.getLength() == 0)
                {
                    path = null;
                }
            }

            if(!hasTarget() && path != null)
            {
                setTarget(path.getFirstStep().getX(), path.getFirstStep().getY());
            }

            moveToTarget();
        }
    }

    private void moveToTarget()
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

    private boolean hasTarget()
    {
        return target != null;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString()
    {
        return ">> " + spriteName + " " + speed + " " + behavior;
    }
}
