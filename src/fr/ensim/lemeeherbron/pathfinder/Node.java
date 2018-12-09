package fr.ensim.lemeeherbron.pathfinder;

public class Node implements Comparable {

    private int x;
    private int y;

    private float cost;
    Node parent;
    private float heuristic;
    private int depth;

    public Node(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int setParent(Node parent)
    {
        depth = parent.depth + 1;
        this.parent = parent;

        return depth;
    }

    public void setDepth(int depth)
    {
        this.depth = depth;
    }

    public void setCost(float cost)
    {
        this.cost = cost;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public float getCost()
    {
        return cost;
    }

    public void setHeuristic(float heuristic)
    {
        this.heuristic = heuristic;
    }

    public Node getParent()
    {
        return parent;
    }

    public int compareTo(Object other)
    {
        Node o = (Node) other;

        float f = heuristic + cost;
        float of = o.heuristic + o.cost;

        if(f < of)
        {
            return -1;
        }
        else
        {
            if(f > of)
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }
    }

}
