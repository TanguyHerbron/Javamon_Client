package fr.ensim.lemeeherbron.entities;

import javafx.scene.canvas.GraphicsContext;

public class Player extends Entity {

    private int counter;
    private boolean walking;

    public Player(String spriteName, double width, double height, double borderX, double borderY, double speed) {
        super(spriteName, width, height, borderX, borderY, speed / 5);
    }

    public void setWalking(boolean walking)
    {
        this.walking = walking;
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
}
