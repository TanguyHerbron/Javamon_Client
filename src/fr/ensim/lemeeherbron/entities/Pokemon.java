package fr.ensim.lemeeherbron.entities;

import fr.ensim.lemeeherbron.terrain.Terrain;
import fr.ensim.lemeeherbron.terrain.pathfinder.AStarPathFinder;
import fr.ensim.lemeeherbron.terrain.pathfinder.Path;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Pokemon extends Entity {

    private static final int FEMALE = 0;
    private static final int MALE = 1;

    private static final AtomicInteger count = new AtomicInteger(0);
    private int id;
    private boolean behavior;
    private Point2D target;
    private Path path;
    private Terrain terrain;
    private int level;
    private float xp;
    private HashMap<Integer, String> evolutions;
    private int sexe = MALE;

    public Pokemon(String spriteName, int width, int height, double borderX, double borderY, int speed, boolean behavior) {
        super("pokemon/" + spriteName, width, height, borderX, borderY, speed);

        this.behavior = behavior;
        id = count.incrementAndGet();

        xp = 0;
        level = 1;
    }

    public Pokemon(String spriteName, double borderX, double borderY, int speed, boolean behavior) {
        this(spriteName, 32, 32, borderX, borderY, speed, behavior);
    }

    public Pokemon(String spriteName, double borderX, double borderY, int speed, boolean behavior, Terrain terrain, int sexe) {
        this(spriteName, 32, 32, borderX, borderY, speed, behavior, terrain);

        this.sexe = sexe;
    }

    public Pokemon(String spriteName, int width, int height, double borderX, double borderY, int speed, boolean behavior, Terrain terrain)
    {
        this(spriteName, width, height, borderX, borderY, speed, behavior);

        this.terrain = terrain;
    }

    public Pokemon(int id, String spriteName, int x, int y, char orientation)
    {
        super(spriteName, 32, 32, 512, 512, 5);

        this.id = id;
        this.x = x;
        this.y = y;
        this.lastMove = orientation;

        terrain = Terrain.getInstance();
        behavior = true;

        xp = 0;
        level = 1;
    }

    public void addEvolutions(int level, String spriteName)
    {
        if(evolutions == null)
        {
            evolutions = new HashMap<>();
        }

        evolutions.put(level, spriteName);
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
            move();
        }
        else
        {
            if(hasBehavior())
            {
                if(new Random().nextInt(20) == 0)
                {
                    Random rand = new Random();

                    int xp = (int) Math.floor(getX() / 16);
                    int xy = (int) Math.floor(getY() / 16);

                    AStarPathFinder aStarPathFinder = new AStarPathFinder(terrain, 100);

                    path = aStarPathFinder.findPath(xp, xy, 3 + rand.nextInt(25), 4 + rand.nextInt(13));
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

        if(target != null && (target.getX() != x || target.getY() != y))
        {
            if(target.getX() != x)
            {
                if(target.getY() < x)
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
                if(target.getY() < y)
                {
                    up(terrain);
                }
                else
                {
                    down(terrain);
                }
            }

            if(Math.abs(target.getX() - getX()) < speed)
            {
                setPosX(target.getX());
            }

            if(Math.abs(target.getY() - getY()) < speed)
            {
                setPosY(target.getY());
            }

            if(getX() == target.getX() && getY() == target.getY())
            {
                target = null;
            }
        }
    }

    public void setTarget(int x, int y)
    {
        target = new Point2D(x, y);
    }

    public double getSpeed()
    {
        return speed;
    }

    public void setSpeed(double speed)
    {
        this.speed = speed;
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
        String str = spriteName.substring(spriteName.lastIndexOf("/") + 1);
        String sexeSym = "♂";

        if(sexe == FEMALE)
        {
            sexeSym = "♀";
        }

        return sexeSym + " " + str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public void pex()
    {
        if(level < 100)
        {
            xp += new Random().nextInt(10);

            float levelThreshold = (float) (100 + (level * (0.65 * level)));

            if(xp > levelThreshold)
            {
                xp = xp % levelThreshold;
                level++;

                if(evolutions != null && evolutions.containsKey(level))
                {
                    spriteName = "pokemon/" + evolutions.get(level);
                    this.image = new Image("/sprite/" + spriteName + ".png");
                }
            }
        }
    }

    public int getSexe()
    {
        return sexe;
    }
}
