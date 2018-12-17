package sample;

import fr.ensim.lemeeherbron.entities.AnimatedSprite;
import fr.ensim.lemeeherbron.entities.Player;
import fr.ensim.lemeeherbron.entities.Sprite;
import fr.ensim.lemeeherbron.terrain.Terrain;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class GameSpine {

    private GraphicsContext graphicsContext;
    private Terrain terrain;
    private List<Sprite> sprites;
    private List<AnimatedSprite> animatedSprites;
    private Player player;

    public GameSpine(GraphicsContext graphicsContext)
    {
        this.graphicsContext = graphicsContext;

        terrain = Terrain.build(0, 0);

        sprites = new ArrayList<>();
        animatedSprites = new ArrayList<>();

        setupPlayer();
    }

    private void setupPlayer()
    {
        player = new Player("scientist",512, 512, 7);
        player.setPosition(25 * 16, 20 * 16);

        sprites.add(player);
    }

    public Player getPlayer()
    {
        return player;
    }

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
        for(Sprite sprite : sprites) {
            sprite.render(graphicsContext);
        }

        int index = 0;

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
                        terrain = Terrain.build("inside1");
                        player.setPosition(240, 288);
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
}
