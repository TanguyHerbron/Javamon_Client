package fr.ensim.lemeeherbron;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class MenuDrawer {

    private Canvas canvas;

    public MenuDrawer(Canvas canvas)
    {
        this.canvas = canvas;
    }

    public void draw()
    {
        int h = (int) Math.round(canvas.getHeight());
        int w = (int) Math.round(canvas.getWidth());

        drawMenuBackground(w, h);

        drawMenuLine(w, 0, "top");
        drawMenuLine(w, h - 8, "bot");
        drawMenuLine(h, 0, "left");
        drawMenuLine(h, w - 8, "right");

        drawMenuAngle(0, 0, 0);
        drawMenuAngle(w - 8, 0, 90);
        drawMenuAngle(0, h - 8, 270);
        drawMenuAngle(w - 8, h - 8, 180);
    }

    private void drawMenuBackground(int width, int height)
    {
        Image background = new Image("/menu/background.png");

        for(int y = 8; y < height - 8; y += background.getHeight())
        {
            for(int x = 8; x < width - 8; x += background.getWidth())
            {
                canvas.getGraphicsContext2D().drawImage(background, x, y);
            }
        }
    }

    private void drawMenuLine(int size, int offset, String side)
    {
        Image line = new Image("/menu/line.png");

        ImageView iv = new ImageView(line);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        switch (side)
        {
            case "top":
                iv.setRotate(0);

                line = iv.snapshot(params, null);

                for(int i = 8; i < size - 8; i+=8)
                {
                    canvas.getGraphicsContext2D().drawImage(line, i, offset);
                }
                break;
            case "bot":
                iv.setRotate(180);

                line = iv.snapshot(params, null);

                for(int i = 8; i < size - 8; i+=8)
                {
                    canvas.getGraphicsContext2D().drawImage(line, i, offset);
                }
                break;
            case "left":
                iv.setRotate(270);

                line = iv.snapshot(params, null);

                for(int i = 8; i < size - 8; i+=8)
                {
                    canvas.getGraphicsContext2D().drawImage(line, offset, i);
                }
                break;
            case "right":
                iv.setRotate(90);

                line = iv.snapshot(params, null);

                for(int i = 8; i < size - 8; i+=8)
                {
                    canvas.getGraphicsContext2D().drawImage(line, offset, i);
                }
                break;
        }
    }

    private void drawMenuAngle(int x, int y, int rotation)
    {
        Image corner = new Image("/menu/corner.png");

        ImageView iv = new ImageView(corner);
        iv.setRotate(rotation);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        corner = iv.snapshot(params, null);

        canvas.getGraphicsContext2D().drawImage(corner, x, y);
    }
}
