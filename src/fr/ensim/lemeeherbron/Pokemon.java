package fr.ensim.lemeeherbron;

public class Pokemon extends Sprite {

    private boolean behavior;

    public Pokemon(String spriteName, double borderX, double boderY) {
        super(spriteName, borderX, boderY);
    }

    public Pokemon(String spriteName, double borderX, double borderY, boolean behavior) {
        super(spriteName, borderX, borderY);

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
