package sample;

import fr.ensim.lemeeherbron.*;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller extends AnimationTimer implements Initializable {

    @FXML private Canvas mainCanvas;
    @FXML private Label fpsLabel;
    @FXML private ImageView imageSettings;
    @FXML private Pane dialogPane;
    @FXML private Canvas dialogCanvas;
    @FXML private CheckBox checkBoxDrawGrid;
    @FXML private CheckBox checkBoxDrawPath;
    @FXML private CheckBox checkBoxShowFPS;
    @FXML private CheckBox checkBoxShowHitBox;

    private GameSpine gameSpine;

    private MenuDrawer menuDrawer;

    //FPS counter variables
    private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0;
    private boolean arrayFilled = false;

    private boolean renderCanvas = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        gameSpine = new GameSpine(mainCanvas.getGraphicsContext2D());

        menuDrawer = new MenuDrawer(dialogCanvas);

        mainCanvas.setFocusTraversable(true);

        mainCanvas.setOnKeyPressed(gameSpine.getPlayer());

        mainCanvas.setOnKeyReleased(gameSpine.getPlayer());

        mainCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                /*int x = (int) Math.floor(event.getX() / 16);
                int y = (int) Math.floor(event.getY() / 16);

                int xp = (int) Math.floor(pikaSprite.getX() / 16);
                int xy = (int) Math.floor(pikaSprite.getY() / 16);

                selectedSprite = new Sprite("selected", 512, 512);
                selectedSprite.setPosition(x * 16, y * 16);

                AStarPathFinder aStarPathFinder = new AStarPathFinder(terrain, 100);

                path = aStarPathFinder.findPath(xp, xy, x, y);*/
            }
        });

        setupSettingsButton();

        start();
    }

    private void setupSettingsButton()
    {
        imageSettings.setImage(new Image("/menu/gear.png"));
        imageSettings.setFitHeight(24);
        imageSettings.setFitWidth(24);

        imageSettings.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                dialogPane.setVisible(!dialogPane.isVisible());
            }
        });

        dialogPane.setVisible(false);
        fpsLabel.setVisible(checkBoxShowFPS.isSelected());
    }

    private void computeFPS(long now)
    {
        long oldFrameTime = frameTimes[frameTimeIndex];
        frameTimes[frameTimeIndex] = now;
        frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
        if(frameTimeIndex == 0)
        {
            arrayFilled = true;
        }

        if(arrayFilled)
        {
            long elapsedNanos = now - oldFrameTime;
            long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
            double frameRate = 1_000_000_000.0 / elapsedNanosPerFrame;
            fpsLabel.setText(String.format("%.2f FPS", frameRate));
        }

    }

    private void fadeOutTransition()
    {
        renderCanvas = false;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mainCanvas.getOpacity() > 0)
                {
                    mainCanvas.setOpacity(mainCanvas.getOpacity() - 0.1);

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                while(!gameSpine.isReady());

                fadeInTransition();
            }
        }).start();
    }

    private void fadeInTransition()
    {
        renderCanvas = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mainCanvas.getOpacity() < 1)
                {
                    mainCanvas.setOpacity(mainCanvas.getOpacity() + 0.1);

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void mouseClicked_showFPS()
    {
        fpsLabel.setVisible(!fpsLabel.isVisible());
    }

    @Override
    public void handle(long now) {
        gameSpine.movePlayer();

        if(renderCanvas) gameSpine.draw();

        if(checkBoxDrawGrid.isSelected()) gameSpine.drawGrid();

        if(checkBoxShowHitBox.isSelected()) gameSpine.drawHitboxs();

        if(dialogPane.isVisible()) menuDrawer.draw();

        if(gameSpine.checkPortal()) fadeOutTransition();

        if(checkBoxShowFPS.isSelected()) computeFPS(now);
    }
}