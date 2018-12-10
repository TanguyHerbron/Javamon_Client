package fr.ensim.lemeeherbron.terrain.pathfinder;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class Path {

    private ArrayList steps = new ArrayList();

    public Path() {}

    public int getLength()
    {
        return steps.size();
    }

    public Step getStep(int index)
    {
        return (Step) steps.get(index);
    }

    public int getX(int index)
    {
        return getStep(index).getX();
    }

    public int getY(int index)
    {
        return getStep(index).getY();
    }

    public void appendStep(int x, int y)
    {
        steps.add(new Step(x, y));
    }

    public void prependStep(int x, int y)
    {
        steps.add(0, new Step(x, y));
    }

    public boolean contains(int x, int y)
    {
        return steps.contains(new Step(x, y));
    }

    public Step getFirstStep()
    {
        return (Step) steps.get(0);
    }

    public void completeFistStep()
    {
        steps.remove(0);
    }

    public void render(GraphicsContext graphicsContext)
    {
        Image image = new Image("/sprite/selected.png");

        for(int i = 0; i < getLength(); i++)
        {
            graphicsContext.drawImage(image, getStep(i).getX(), getStep(i).getY());
        }
    }
}
