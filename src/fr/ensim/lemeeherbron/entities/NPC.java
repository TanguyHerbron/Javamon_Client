package fr.ensim.lemeeherbron.entities;

import fr.ensim.lemeeherbron.MenuDrawer;
import javafx.scene.canvas.GraphicsContext;

public class NPC extends Entity {

    private boolean showMenu = false;

    public NPC(String spriteName)
    {
        this(spriteName, 0, 0);

        this.obstacle = true;
    }

    public NPC(String spriteName, double borderX, double borderY) {
        super("npc/" + spriteName, 32, 32, borderX, borderY, 0);
    }

    public void interact()
    {
        showMenu = !showMenu;
    }

    public boolean interacts()
    {
        return showMenu;
    }
}
