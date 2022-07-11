package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.jsfml.graphics.*;
import org.jsfml.system.*;

import ManageAnimation.Transition;
import devtools.ObservableEvent;
import devtools.Observer;
import hero.Hero;
import models.Critter;
import screens.DeathScreen;
import ui.Overlay;
import ui.UIOverlay;
// import views.*;
import platformer.GameScene;
import sys.Renderable;
import sys.Renderer;

/**
 * The class which takes care of drawing things on the screen.
 * 
 * 
 */
public class GameRenderer implements Renderer, Observer {
    public final Game game;

    private final List<Renderable> renderingCollection;
    private GameScene scene; // the game window with critters and all

    // UI elements
    private Drawable ui;
    // final View heroView;

    // Different rendering lists
    private List<Renderable> defaultList = new ArrayList<>();
    private List<Renderable> sceneList = new ArrayList<>(); // scene elements - for play mode
    // private final List<Drawable> uiList = new ArrayList<>(); // ui elements - for play mode
    // private final List<Renderable> homescreenList = new ArrayList<>(); // home screen elements - for home mode
    // private final List<Renderable> menuList = new ArrayList<>(); // menuList
    private List<Drawable> overlayList = new ArrayList<>();

    private int drawn = 0; // nb of objects drawn during tick

    public GameRenderer(Game game) {
        this.game = game;
        this.renderingCollection = new ArrayList<>();
        this.scene = game.scene;
        // this.heroView = new View(new FloatRect(Vector2f.ZERO, new Vector2f(game.window.getSize())));
        // System.out.println("Hero view: " + heroView.getCenter() +", " + heroView.getSize());
    }

    public Collection<Renderable> getRenderingCollection() {
        return this.renderingCollection;
    }

    public void resetRenderer() {
        defaultList = new ArrayList<>();
        sceneList = new ArrayList<>();
        overlayList = new ArrayList<>();
    } 

    @Override
    public void addToRenderingList(Renderable renderable) {
        this.renderingCollection.add(renderable);
        // if (renderable == game.scene) {
        //     this.defaultList.add(renderable);
        // } else 
        if (renderable instanceof Critter) {
            // System.out.println("Pixel position: " + renderable.getPosition());
            this.sceneList.add(renderable);
        }
        else if (renderable instanceof Overlay) {
            this.overlayList.add(renderable);
        }
    }

    public void add(Drawable drawable) {
        if (drawable instanceof Renderable) {
            this.addToRenderingList((Renderable)drawable);
            return;
        }

        if (drawable == game.ui) {
            ui = drawable;
        }

        if (drawable instanceof Transition) {
            this.overlayList.add(drawable);
        }
    }

    public void remove(Drawable drawable) {
        if (drawable instanceof Transition) {
            this.overlayList.remove(drawable);
        }
    }

    @Override
    public void removeFromRenderingList(Renderable renderable) {
        if (renderable == game.scene) {
            this.defaultList.remove(renderable);
        } else if (renderable instanceof Critter) {
            this.sceneList.remove(renderable);
        }
        else if (renderable instanceof Overlay) {
            this.overlayList.remove(renderable);
        }
        this.renderingCollection.remove(renderable);
    }

    @Deprecated
    public void render() {
        // System.out.println("The critters have been updated in the controller, and the
        // View renders them");
        // Order the collection by their layers (what is drawn first)
        // then draw everything
        this.renderingCollection.sort(Comparator.comparing(Renderable::getLayer));
        // int renderSize = this.renderingCollection.size();
        for (Renderable drawable : this.renderingCollection) {
            // the critters are already ordered
            this.game.window.draw(drawable);
        }
    }

    public void render(RenderWindow window) {

        GameState state = this.game.getState();

        if (state == GameState.HOME_SCREEN) {
            window.draw(game.homescreen);
            return;
        }

        if (state == GameState.DEATH){
            window.draw(game.deathScreen);
            return;
        }

        // if (state.isPlaying()) {
        //     // The platformer is almost always rendered (except during home screen)
        //     // Render, in this order, the platformer, the elements of the platformer view
        //     // and the ui
        //     window.setView(window.getDefaultView());
        //     window.draw(game.scene);
        //     window.setView(game.scene.view);
        //     sceneList.sort(Comparator.comparing(Renderable::getLayer));
        //     sceneList.forEach((Renderable r) -> {
        //         window.draw(r);
        //         drawn++;
        //     });

        //     window.setView(window.getDefaultView());
        //     window.draw(ui);

        //     if (state == GameState.PAUSED) {
        //         window.setView(window.getDefaultView());
        //         window.draw(game.pauseOverlay);
        //     }

        //     window.setView(window.getDefaultView());
        //     overlayList.forEach(window::draw);
        // }

        if (state != GameState.HOME_SCREEN && state != GameState.DEATH) {
            // The platformer is almost always rendered (except during home screen)
            // Render, in this order, the platformer, the elements of the platformer view
            // and the ui
            window.setView(window.getDefaultView());
            window.draw(game.scene);
            window.setView(game.scene.view);
            sceneList.sort(Comparator.comparing(Renderable::getLayer));
            sceneList.forEach((Renderable r) -> {
                window.draw(r);
                drawn++;
            });

            window.setView(window.getDefaultView());
            window.draw(ui);

            window.setView(window.getDefaultView());
            overlayList.forEach(window::draw);
        }

        if (state == GameState.PAUSED) {
            // Render the menu on top of the game play (but below the UI)
            window.setView(window.getDefaultView());
            window.draw(game.pauseOverlay);
        }
    }

    @Override
    public void receiveUpdate(ObservableEvent event) {
        if (event.getEventID() == Hero.EVENT_HERO_MOVED) {
            this.updateCamera((Vector2f)event.getSource());
            // the amount by which the player has moved
            // Vector2f displacement = (Vector2f) event.getSource();
            // // System.out.println("Limit: " + game.scene.getSceneSize().x/3 + ", pos = "+game.hero.getPosition().x);
            // if (displacement.x > 0 && game.hero.getPixelPosition(game.window).x > game.scene.getSceneSize().x/2){
            // // if (displacement.x > 0) {
            //     this.game.hero.view.move(new Vector2f(displacement.x, 0));
            //     this.game.scene.view.move(new Vector2f(6, 0));
            //     // this.game.scene.view.move(new Vector2f(2*displacement.x, 0));
            //     // this.game.scene.scroll(new Vector2f(displacement.x, 0));
            // }

        }
    }

    protected void updateCamera(Vector2f displacement) {
        // Follow the hero
        Vector2i pixels = game.hero.getPixelPosition(game.window);
        final Vector2f display = new Vector2f(game.window.getSize());
        if (pixels.x > display.x/2 && displacement.x > 0) {
            game.scene.view.move(displacement.x,0);
        }


    }

    
}
