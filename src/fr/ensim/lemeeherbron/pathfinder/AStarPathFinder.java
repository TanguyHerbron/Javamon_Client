package fr.ensim.lemeeherbron.pathfinder;

import fr.ensim.lemeeherbron.Terrain;

import java.util.ArrayList;

public class AStarPathFinder {

    private Terrain terrain;

    private ArrayList closed;

    private SortedList open;

    private Node[][] nodes;
    private int maxSearchDistance;

    public AStarPathFinder(Terrain terrain, int maxSearchDistance)
    {
        this.terrain = terrain;
        this.maxSearchDistance = maxSearchDistance;

        closed = new ArrayList();

        open = new SortedList();

        nodes = new Node[terrain.getWidth()][terrain.getHeight()];

        for(int x = 0; x < terrain.getWidth(); x++)
        {
            for(int y = 0; y < terrain.getHeight(); y++)
            {
                nodes[x][y] = new Node(x, y);
            }
        }
    }

    public Path findPath(int sx, int sy, int tx, int ty)
    {
        if(terrain.blocked(tx, ty))
        {
            return null;
        }

        nodes[sx][sy].setCost(0);
        nodes[sx][sy].setDepth(0);
        closed.clear();
        open.clear();
        open.add(nodes[sx][sy]);

        nodes[tx][ty].parent = null;

        int maxDepth = 0;
        while((maxDepth < maxSearchDistance) && (open.size() != 0))
        {
            Node currentNode = getFirstInOpen();
            if(currentNode != nodes[tx][ty])
            {
                removeFromOpen(currentNode);
                addToClosed(currentNode);

                for(int x = -1; x < 2; x++)
                {
                    for(int y = -1; y < 2; y++)
                    {
                        if((x == 0) && (y == 0))
                        {
                            continue;
                        }

                        if((x != 0) && (y != 0))
                        {
                            continue;
                        }

                        int xp = x + currentNode.getX();
                        int yp = y + currentNode.getY();

                        if(isValidLocation(sx, sy, xp, yp))
                        {
                            float nextStepCost = currentNode.getCost() + 1;
                            Node neighbour = nodes[xp][yp];
                            //TODO remove line if never used
                            //terrain.pathFinderVisited()

                            if(nextStepCost < neighbour.getCost())
                            {
                                if(inOpenList(neighbour))
                                {
                                    removeFromOpen(neighbour);
                                }

                                if(inClosedList(neighbour))
                                {
                                    removeFromClosed(neighbour);
                                }
                            }

                            if(!inOpenList(neighbour) && !inClosedList(neighbour))
                            {
                                neighbour.setCost(nextStepCost);
                                neighbour.setHeuristic(getHeuristic(xp, yp, tx, ty));
                                maxDepth = Math.max(maxDepth, neighbour.setParent(currentNode));
                                addToOpen(neighbour);
                            }
                        }
                    }
                }
            }
            else
            {
                break;
            }
        }

        if(nodes[tx][ty].getParent() == null)
        {
            return null;
        }

        Path path = new Path();
        Node target = nodes[tx][ty];

        while(target != nodes[sx][sy])
        {
            path.prependStep(target.getX() * 16, target.getY() * 16);
            target = target.getParent();
        }

        path.prependStep(sx * 16, sy * 16);

        return path;
    }

    public float getHeuristic(int x, int y, int tx, int ty)
    {
        float dx = tx - x;
        float dy = ty - y;

        return (float) (Math.sqrt((dx * dx) + (dy * dy)));
    }

    public boolean inOpenList(Node n)
    {
        return open.contains(n);
    }

    public boolean inClosedList(Node n)
    {
        return closed.contains(n);
    }

    public boolean isValidLocation(int sx, int sy, int x, int y)
    {
        boolean invalid = (x < 0) || (y < 0) || (x >= terrain.getWidth()) || (y >= terrain.getHeight());


        if((!invalid) && ((sx != x) || (sy != y)))
        {
            invalid = terrain.blocked(x, y);
        }

        return !invalid;
    }

    private void addToOpen(Node n)
    {
        open.add(n);
    }

    private void addToClosed(Node n)
    {
        closed.add(n);
    }

    private void removeFromOpen(Node n)
    {
        open.remove(n);
    }

    public void removeFromClosed(Node n)
    {
        closed.remove(n);
    }

    private Node getFirstInOpen()
    {
        return (Node) open.first();
    }
}
