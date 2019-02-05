package fr.ensim.lemeeherbron.entities;

import fr.ensim.lemeeherbron.PokemonBuilder;
import fr.ensim.lemeeherbron.PokemonNotLoadedException;
import fr.ensim.lemeeherbron.entities.interaction.Dialog;
import fr.ensim.lemeeherbron.terrain.Terrain;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity implements EventHandler<KeyEvent> {

    private int counter;
    private boolean walking;

    private char direction = '0';
    private int numberKeyPressed;

    private Dialog currentDialog;

    private List<Pokemon> pokemons;

    public Player(String spriteName, double borderX, double borderY, double speed) {
        super("npc/" + spriteName, 32, 32, borderX, borderY, speed / 5);

        pokemons = new ArrayList<>();

        try {
            pokemons.add(PokemonBuilder.build("ptera", 512, 512));
        } catch (PokemonNotLoadedException e) {
            e.printStackTrace();
        }
    }

    public char getDirection()
    {
        return direction;
    }

    @Override
    public void render(GraphicsContext graphicsContext)
    {
        int xPiece = 0;
        int yPiece = 0;

        switch (lastMove)
        {
            case 'u':
                if(walking)
                {
                    if(counter > 15)
                    {
                        xPiece = 64;
                        yPiece = 0;
                    }
                    else
                    {
                        xPiece = 32;
                        yPiece = 96;
                    }
                }
                else
                {
                    xPiece = 0;
                    yPiece = 0;
                }
                break;
            case 'd':
                if(walking)
                {
                    if(counter > 15)
                    {
                        xPiece = 64;
                        yPiece = 96;
                    }
                    else
                    {
                        xPiece = 64;
                        yPiece = 64;
                    }
                }
                else
                {
                    xPiece = 64;
                    yPiece = 32;
                }
                break;
            case 'l':
                if(walking)
                {
                    if(counter > 15)
                    {
                        xPiece = 0;
                        yPiece = 32;
                    }
                    else
                    {
                        xPiece = 0;
                        yPiece = 96;
                    }
                }
                else
                {
                    xPiece = 0;
                    yPiece = 64;
                }
                break;
            case 'r':
                if(walking)
                {
                    if(counter > 15)
                    {
                        xPiece = 32;
                        yPiece = 64;
                    }
                    else
                    {
                        xPiece = 32;
                        yPiece = 32;
                    }
                }
                else
                {
                    xPiece = 32;
                    yPiece = 0;
                }
        }

        counter = (counter + 1) % 31;

        graphicsContext.drawImage(image, xPiece, yPiece, width, height, x, y, width, height);
    }

    @Override
    public void handle(KeyEvent event) {
        if(event.getEventType() == KeyEvent.KEY_PRESSED && currentDialog == null)
        {
            keyPressedEvent(event.getCode());
        }

        if(event.getEventType() == KeyEvent.KEY_RELEASED)
        {
            keyReleasedEvent(event.getCode());
        }
    }

    private void keyPressedEvent(KeyCode keyCode)
    {
        switch (keyCode)
        {
            case Z:
                if(direction != 'u')
                {
                    direction = 'u';
                    numberKeyPressed++;
                    walking = true;
                }
                break;
            case S:
                if(direction != 'd')
                {
                    direction = 'd';
                    numberKeyPressed++;
                    walking = true;
                }
                break;
            case Q:
                if(direction != 'l')
                {
                    direction = 'l';
                    numberKeyPressed++;
                    walking = true;
                }
                break;
            case D:
                if(direction != 'r')
                {
                    direction = 'r';
                    numberKeyPressed++;
                    walking = true;
                }
                break;
        }
    }

    private void keyReleasedEvent(KeyCode keyCode)
    {
        if(keyCode.equals(KeyCode.Z) || keyCode.equals(KeyCode.Q) || keyCode.equals(KeyCode.S) || keyCode.equals(KeyCode.D))
        {
            numberKeyPressed--;
        }

        if(numberKeyPressed == 0)
        {
            walking = false;
            direction = '0';
        }

        if(keyCode.equals(KeyCode.E))
        {

            walking = false;
            direction = '0';
            numberKeyPressed = 0;

            if(currentDialog == null)
            {
                 lookForInteraction();
            }
            else
            {
                currentDialog = currentDialog.getNextDialog();
            }
        }
    }

    private void lookForInteraction()
    {
        double lookingX = getBoundary().getMinX();
        double lookingY = getBoundary().getMinY();

        int width = 16;
        int height = 16;

        boolean found = false;
        int index = 0;
        List<Sprite> obstacles = Terrain.getInstance().getObstacleList();

        switch (lastMove)
        {
            case 'u':
                lookingY -= 4;
                height = 4;
                break;
            case 'd':
                lookingY += 16;
                height = 4;
                break;
            case 'l':
                lookingX -= 4;
                width = 4;
                break;
            case 'r':
                lookingX += 16;
                width = 4;
                break;
        }

        Rectangle2D lookingRec = new Rectangle2D(lookingX, lookingY, width, height);

        while(!found && index < obstacles.size())
        {
            if(obstacles.get(index) instanceof NPC && obstacles.get(index).getBoundary().intersects(lookingRec))
            {
                found = true;

                this.currentDialog = ((NPC) obstacles.get(index)).interact(lastMove);
            }

            index++;
        }
    }

    public void updateDialog()
    {
        currentDialog = currentDialog.getNextDialog();
    }

    public void updateDialog(int index)
    {
        currentDialog = currentDialog.getDialog(index);
    }

    public Dialog getDialog()
    {
        return currentDialog;
    }

    public boolean interacts()
    {
        return currentDialog != null;
    }

    public List<Pokemon> getPokemonList()
    {
        return pokemons;
    }

    public void removePokemon(Pokemon pokemon)
    {
        pokemons.remove(pokemon);
    }

    public void givePokemon(Pokemon pokemon)
    {
        pokemons.add(pokemon);
    }
}
