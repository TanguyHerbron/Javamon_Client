package fr.ensim.lemeeherbron;

public class Pokemon extends Sprite {

    private boolean behavior;

    public Pokemon(String spriteName) {
        super(spriteName);
    }

    public Pokemon(String spriteName, boolean behavior) {
        super(spriteName);

        this.behavior = behavior;
    }

    public void setBehavior(boolean behavior)
    {
        this.behavior = behavior;
    }

    public boolean hasBehavior()
    {
        return behavior;
    }

}
