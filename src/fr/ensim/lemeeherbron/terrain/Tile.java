package fr.ensim.lemeeherbron.terrain;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Tile {

    private Image image;
    private String tilePath;

    private int randomTextNumber;

    private boolean tl = false;
    private boolean tr = false;
    private boolean bl = false;
    private boolean br = false;

    private int x;
    private int y;

    private int orgX;
    private int orgY;

    public Tile(String tilePath, int x, int y, boolean isVariant)
    {
        image = new Image("/tile/" + tilePath + ".png");

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
        image = new Image("/tile/" + tilePath + ".png");

        this.randomTextNumber = random;

        this.x = x;
        this.y = y;
    }

    public void render(GraphicsContext graphicsContext)
    {
        graphicsContext.drawImage(image, orgX, orgY, 16, 16, x, y, 16, 16);
    }

    public void construct()
    {
        if(image != null)
        {
            if(randomTextNumber != 0)
            {
                if(Math.random() * 10 > 9)
                {
                    switch ((int) Math.round(Math.random() * randomTextNumber))
                    {
                        case 0:
                            orgX = 17;
                            orgY = 0;
                            break;
                        case 1:
                            orgX = 0;
                            orgY = 17;
                            break;
                        case 2:
                            orgX = 17;
                            orgY = 17;
                            break;
                        default :
                            orgX = 0;
                            orgY = 0;
                            break;
                    }
                }
                else
                {
                    orgX = 0;
                    orgY = 0;
                }
            }
            else
            {
                selectVariant();
            }
        }
    }

    private void selectVariant()
    {
        if(!tl && !tr)
        {
            orgY = 0;

            computeOrgX(bl, br);
        }
        else
        {
            if(!bl && !br)
            {
                orgY = 34;

                computeOrgX(tl, tr);
            }
            else
            {
                if(Stream.of(tl, tr, bl, br).filter(p -> !p).count() == 1)
                {
                    orgY = 51;

                    if(!br) orgX = 0;
                    else if(!bl) orgX = 17;
                    else
                    {
                        orgY = 67;

                        if(!tr) orgX = 0;
                        else if(!tl) orgX = 17;
                    }
                }
                else
                {
                    orgY = 17;

                    computeOrgX(tl && bl, tr && br);
                }
            }
        }
    }

    private void computeOrgX(boolean b1, boolean b2)
    {
        if(!b1) orgX = 0;
        else if (!b2) orgX = 34;
        else orgX = 17;
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

    public int getWidth()
    {
        return (int) Math.round(image.getWidth());
    }

    public int getHeight()
    {
        return (int) Math.round(image.getHeight());
    }
}
