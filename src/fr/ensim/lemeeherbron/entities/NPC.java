package fr.ensim.lemeeherbron.entities;

import javafx.scene.canvas.GraphicsContext;

public class NPC extends Entity {

    public NPC(String spriteName, double borderX, double borderY) {
        super("npc/" + spriteName, 32, 32, borderX, borderY, 0);
    }

    public void interact()
    {

    }

    @Override
    public void render(GraphicsContext graphicsContext)
    {
        super.render(graphicsContext);

        //Render UI window for the dialog
    }
}
