package fr.ensim.lemeeherbron.entities;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Player extends Entity implements EventHandler<KeyEvent> {

    private int counter;
    private boolean walking;

    private char direction = '0';
    private int numberKeyPressed;

    public Player(String spriteName, double width, double height, double borderX, double borderY, double speed) {
        super("npc/" + spriteName, width, height, borderX, borderY, speed / 5);
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
        if(event.getEventType() == KeyEvent.KEY_PRESSED)
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
            direction = '0';
            walking = false;
        }
    }
}
