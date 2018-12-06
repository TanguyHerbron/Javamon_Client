package fr.ensim.lemeeherbron;

import javafx.scene.canvas.GraphicsContext;

public class AnimatedSprite extends Thread {

    private int size;
    private Sprite[] sprites;
    private int state;
    private boolean repeat;

    public AnimatedSprite(String spritePath, double borderX, double borderY, int size, double x, double y)
    {
        sprites = new Sprite[size];

        for(int i = 0; i < sprites.length; i++)
        {
            sprites[i] = new Sprite(spritePath + "_" + i, borderX, borderY);
            sprites[i].setPosition(x, y);
        }

        this.size = size;

        state = 0;

        repeat = false;
    }

    public synchronized void start(boolean repeat) {
        super.start();

        this.repeat = repeat;
    }

    @Override
    public void run() {
        super.run();

        do {
            state = (state + 1) % size;

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (repeat || state != 0);
    }

    public int getAnimationState()
    {
        return state;
    }

    public void render(GraphicsContext graphicsContext)
    {
        sprites[state].render(graphicsContext);
    }
}
