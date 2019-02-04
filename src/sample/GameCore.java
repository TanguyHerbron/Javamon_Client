package sample;

import com.sun.javafx.geom.Vec2d;
import fr.ensim.lemeeherbron.entities.*;
import fr.ensim.lemeeherbron.terrain.Nursery;
import fr.ensim.lemeeherbron.terrain.Terrain;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameCore {

    private GraphicsContext graphicsContext;
    private Terrain terrain;
    private List<Sprite> sprites;
    private List<AnimatedSprite> animatedSprites;
    private Player player;

    public GameCore(GraphicsContext graphicsContext)
    {
        this.graphicsContext = graphicsContext;

        terrain = Terrain.build("nursery");

        Nursery.init();

        sprites = new ArrayList<>();
        animatedSprites = new ArrayList<>();

        setupPlayer();
    }

    private void setupPlayer()
    {
        player = new Player("scientist",512, 512, 8);
        Vec2d pos = terrain.getSpawnPointFor("00");
        player.setPosition(pos.x * 16, pos.y * 16);

        sprites.add(player);
    }

    public Player getPlayer()
    {
        return player;
    }

    public Terrain getTerrain() { return terrain; }

    public void movePlayer()
    {
        switch (player.getDirection())
        {
            case 'u':
                player.up(terrain);
                break;
            case 'd':
                player.down(terrain);
                break;
            case 'l':
                player.left(terrain);
                break;
            case 'r':
                player.right(terrain);
                break;
        }
    }

    public boolean isReady()
    {
        return terrain.isReady();
    }

    public void draw()
    {
        drawBackground();

        drawSprites();
    }

    private void drawSprites()
    {
        List<Sprite> renderedSprites = new ArrayList<>();
        renderedSprites.addAll(sprites);
        renderedSprites.addAll(terrain.getObstacleList());
        renderedSprites.addAll(Nursery.getPokemons());

        //With this sort, each sprite are then rendered by Y coordinate order, which mean if a sprite is behind another in 2D
        //it's well rendered behind the other sprite instead of being drawn on top of it
        renderedSprites.sort(new Comparator<Sprite>() {
            @Override
            public int compare(Sprite o1, Sprite o2) {
                if(o1.getSpriteName().equals("carpet"))
                {
                    return -1;
                }

                if(o2.getSpriteName().equals("carpet"))
                {
                    return 1;
                }

                return Double.compare(o1.getBoundary().getMinY(), o2.getBoundary().getMinY());
            }
        });

        int index = 0;

        for(Sprite sprite : renderedSprites)
        {
            sprite.render(graphicsContext);
        }

        while(index < animatedSprites.size())
        {
            if(animatedSprites.get(index).getAnimationState() == 0)
            {
                animatedSprites.remove(animatedSprites.get(index));
            }
            else
            {
                animatedSprites.get(index).render(graphicsContext);
            }

            index++;
        }
    }

    private void drawBackground()
    {
        terrain.render(graphicsContext);
    }

    public boolean checkPortal()
    {
        boolean switchMap = false;

        for(Sprite obs : terrain.getObstacleList())
        {
            if(player.intersects(obs) == 2)
            {
                player.setPosition(-1, -1);

                switch (terrain.getValue())
                {
                    case "00":
                        String value = terrain.getValue();

                        terrain = Terrain.build("inside1");

                        Vec2d pos = terrain.getSpawnPointFor(value);

                        player.setPosition(pos.x * 16, pos.y * 16);

                        switchMap = true;
                        break;
                    case "inside1":
                        terrain = Terrain.build(0, 0);
                        player.setPosition(304, 256);
                        switchMap = true;
                        break;
                }

                sprites.clear();
                sprites.add(player);
            }
        }

        return switchMap;
    }

    public void drawHitboxs()
    {
        for(Sprite sprite : sprites)
        {
            Rectangle2D rec = sprite.getBoundary();
            graphicsContext.setFill(Color.BLUE);
            graphicsContext.fillRect(rec.getMinX(), rec.getMinY(), rec.getWidth(), rec.getHeight());
        }

        for(Sprite sprite : terrain.getObstacleList())
        {
            Rectangle2D rec = sprite.getBoundary();
            graphicsContext.setFill(Color.BLUE);
            graphicsContext.fillRect(rec.getMinX(), rec.getMinY(), rec.getWidth(), rec.getHeight());
        }
    }

    public void drawGrid()
    {
        terrain.drawGrid(graphicsContext);
    }

    public void addPokemonToNursery(Pokemon pokemon)
    {
        Nursery.addPokemon(pokemon);
    }
}
