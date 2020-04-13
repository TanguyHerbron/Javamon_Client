package fr.ensim.lemeeherbron.terrain;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

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
        graphicsContext.drawImage(image, x, y);
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
                            orgX = 16;
                            orgY = 0;
                            break;
                        case 1:
                            orgX = 0;
                            orgY = 16;
                            break;
                        case 2:
                            orgX = 16;
                            orgY = 16;
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

        loadImage();
    }

    private void loadImage()
    {
        WritableImage resampledImage = new WritableImage(16, 16);

        PixelReader reader = image.getPixelReader();
        PixelWriter writer = resampledImage.getPixelWriter();
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                Color color = reader.getColor(orgX + x, orgY + y);
                if (color.isOpaque()) {
                    writer.setColor(x, y, color);
                }
            }
        }

        image = resampledImage;
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
                orgY = 32;

                computeOrgX(tl, tr);
            }
            else
            {
                if(Stream.of(tl, tr, bl, br).filter(p -> !p).count() == 1)
                {
                    orgY = 48;

                    if(!br) orgX = 0;
                    else if(!bl) orgX = 16;
                    else
                    {
                        orgY = 64;

                        if(!tr) orgX = 0;
                        else if(!tl) orgX = 16;
                    }
                }
                else
                {
                    orgY = 16;

                    computeOrgX(tl && bl, tr && br);
                }
            }
        }
    }

    private void computeOrgX(boolean b1, boolean b2)
    {
        if(!b1) orgX = 0;
        else if (!b2) orgX = 32;
        else orgX = 16;
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
