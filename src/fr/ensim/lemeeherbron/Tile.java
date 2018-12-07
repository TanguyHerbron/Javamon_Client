package fr.ensim.lemeeherbron;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Tile {

    private Image image;
    private String tilePath;

    private boolean tl = false;
    private boolean tr = false;
    private boolean bl = false;
    private boolean br = false;

    private int x;
    private int y;

    public Tile(String tilePath, int x, int y, boolean isVariant)
    {
        this.tilePath = tilePath;

        this.x = x;
        this.y = y;

        tl = isVariant;
        tr = isVariant;
        bl = isVariant;
        br = isVariant;
    }

    public Tile(boolean isVariant)
    {
        tl = isVariant;
        tr = isVariant;
        bl = isVariant;
        br = isVariant;
    }

    public Tile(String tilePath, int x, int y, int random)
    {
        image = new Image("/tile/" + tilePath + "_" + (((int) Math.round(Math.random() * random))) + ".png");

        this.x = x;
        this.y = y;
    }

    public void render(GraphicsContext graphicsContext)
    {
        graphicsContext.drawImage(image, x, y);
    }

    public void construct()
    {
        if(image == null && tilePath != null)
        {
            String str = "/tile/" + tilePath + "_" + (tl ? 1 : 0) + (tr ? 1 : 0) + (bl ? 1 : 0) + (br ? 1 : 0) + ".png";
            try {
                image = new Image(str);
            } catch (IllegalArgumentException e) {
                System.out.println("Error for " + str);
                image = new Image("/tile/grass_0.png");
            }
        }
    }

    public boolean getTl() {
        return tl;
    }

    public void setTl(boolean tl) {
        this.tl = tl;
    }

    public boolean getTr() {
        return tr;
    }

    public void setTr(boolean tr) {
        this.tr = tr;
    }

    public boolean getBl() {
        return bl;
    }

    public void setBl(boolean bl) {
        this.bl = bl;
    }

    public boolean getBr() {
        return br;
    }

    public void setBr(boolean br) {
        this.br = br;
    }

    public boolean isVariant()
    {
        return tl || tr || bl || br;
    }

    public String toString()
    {
        return ">> " + tl + " " + tr + " " + bl + " " + br;
    }
}
