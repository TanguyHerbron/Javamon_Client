package fr.ensim.lemeeherbron.entities;

import fr.ensim.lemeeherbron.MenuDrawer;
import fr.ensim.lemeeherbron.entities.interaction.Dialog;
import javafx.scene.canvas.GraphicsContext;

public class NPC extends Entity {

    private boolean showMenu = false;

    private int imagePosX;
    private int imagePosY;

    private Dialog dialog;

    public NPC(String spriteName, char orientation)
    {
        this(spriteName, 0, 0);

        this.obstacle = true;

        dialog = new Dialog("Hello");
        dialog.setNextDialog(new Dialog("Goodbye"));

        switchSide(orientation);
    }

    public void switchSide(char orientation)
    {
        try {
            switch (orientation)
            {
                case 'r':
                    imagePosX = 0;
                    imagePosY = 64;
                    break;
                case 'l':
                    imagePosY = 0;
                    imagePosX = 32;
                    break;
                case 'u':
                    imagePosX = 64;
                    imagePosY = 32;
                    break;
                case 'd':
                    imagePosX = 0;
                    imagePosY = 0;
                    break;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public NPC(String spriteName, double borderX, double borderY) {
        super("npc/" + spriteName, 32, 32, borderX, borderY, 0);
    }

    public Dialog interact(char side)
    {
        showMenu = !showMenu;

        switchSide(side);

        return dialog;
    }

    public boolean interacts()
    {
        return showMenu;
    }

    @Override
    public void render(GraphicsContext graphicsContext)
    {
        graphicsContext.drawImage(image, imagePosX, imagePosY, width, height, x, y, width, height);
    }
}
