package sample;

import fr.ensim.lemeeherbron.*;
import fr.ensim.lemeeherbron.entities.Pokemon;
import fr.ensim.lemeeherbron.networking.ClientManager;
import fr.ensim.lemeeherbron.terrain.Nursery;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller extends AnimationTimer implements Initializable {

    @FXML private Canvas mainCanvas;
    @FXML private Label fpsLabel;
    @FXML private Label dialogLabel;
    @FXML private ImageView imageSettings;
    @FXML private Pane settingsPane;
    @FXML private Pane dialogPane;
    @FXML private Canvas settingsCanvas;
    @FXML private Canvas dialogCanvas;
    @FXML private CheckBox checkBoxDrawGrid;
    @FXML private CheckBox checkBoxDrawPath;
    @FXML private CheckBox checkBoxShowFPS;
    @FXML private CheckBox checkBoxShowHitBox;
    @FXML private ListView choiceList;

    private GameCore gameCore;
    private ClientManager clientManager;

    private MenuDrawer settingsDrawer;
    private MenuDrawer dialogDrawer;

    //FPS counter variables
    private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0;
    private boolean arrayFilled = false;

    private boolean renderCanvas = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        gameCore = new GameCore(mainCanvas.getGraphicsContext2D());

        settingsDrawer = new MenuDrawer(settingsCanvas);

        dialogDrawer = new MenuDrawer(dialogCanvas);

        mainCanvas.setFocusTraversable(true);

        mainCanvas.setOnKeyPressed(gameCore.getPlayer());

        mainCanvas.setOnKeyReleased(gameCore.getPlayer());

        mainCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                int x = (int) Math.floor(event.getX() / 16);
                int y = (int) Math.floor(event.getY() / 16);

                System.out.println("Clicked on " + x + " " + y);

                /*int xp = (int) Math.floor(pikaSprite.getX() / 16);
                int xy = (int) Math.floor(pikaSprite.getY() / 16);

                selectedSprite = new Sprite("selected", 512, 512);
                selectedSprite.setPosition(x * 16, y * 16);

                AStarPathFinder aStarPathFinder = new AStarPathFinder(terrain, 100);

                path = aStarPathFinder.findPath(xp, xy, x, y);*/
            }
        });

        setupSettingsButton();

        setupChoiceList();

        try {
            clientManager = new ClientManager("localhost");
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    if(gameCore.getPlayer().getDialog() == null)
                    {
                        gameCore.movePlayer();
                    }

                    Nursery.triggerBehaviors();

                    clientManager.sendPokemons(Nursery.getPokemons());

                    gameCore.updateServerPokemons(clientManager.updatePokemons());

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        start();
    }

    private void setupChoiceList()
    {
        choiceList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                String pokemonName = choiceList.getSelectionModel().getSelectedItems().get(0).toString().substring(choiceList.getSelectionModel().getSelectedItems().get(0).toString().indexOf("/"));

                Pokemon newPokemon = new Pokemon(pokemonName, 512, 512, 5, true, gameCore.getTerrain());
                newPokemon.setPosition(256, 100);

                gameCore.addPokemonToNursery(newPokemon);

                gameCore.getPlayer().updateDialog();

                gameCore.getPlayer().removePokemon(choiceList.getSelectionModel().getSelectedItems().get(0).toString());

                choiceList.setVisible(false);
                choiceList.getItems().removeAll(choiceList.getItems());
            }
        });
    }

    private void setupSettingsButton()
    {
        imageSettings.setImage(new Image("/menu/gear.png"));
        imageSettings.setFitHeight(24);
        imageSettings.setFitWidth(24);

        imageSettings.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                settingsPane.setVisible(!settingsPane.isVisible());
            }
        });

        settingsPane.setVisible(false);
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

                while(!gameCore.isReady());

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
        if(renderCanvas) gameCore.draw();

        if(checkBoxDrawGrid.isSelected()) gameCore.drawGrid();

        if(checkBoxShowHitBox.isSelected()) gameCore.drawHitboxs();

        if(settingsPane.isVisible()) settingsDrawer.draw();

        if(gameCore.checkPortal()) fadeOutTransition();

        if(checkBoxShowFPS.isSelected()) computeFPS(now);

        if(gameCore.getPlayer().interacts())
        {
            if(!dialogPane.isVisible())
            {
                dialogPane.setVisible(true);
            }

            if(gameCore.getPlayer().getDialog() != null)
            {
                String str = " ";

                if(gameCore.getPlayer().getDialog().mustChoosePokemon() && choiceList.getItems().size() == 0)
                {
                    List<Pokemon> pokemonList = gameCore.getPlayer().getPokemonList();

                    choiceList.setVisible(true);

                    for(int i = 0; i < pokemonList.size(); i++)
                    {
                        choiceList.getItems().add(choiceList.getItems().size(), pokemonList.get(i).getSpriteName());
                    }
                }

                dialogLabel.setText(gameCore.getPlayer().getDialog().getText() + str);
                dialogDrawer.draw();
            }
            else
            {
                dialogPane.setVisible(false);
            }
        }
        else
        {
            dialogPane.setVisible(false);
        }
    }
}