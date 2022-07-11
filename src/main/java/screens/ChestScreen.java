package screens;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import Collectibles.Collectible;
import assets.FontCatalogue;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.event.Event;

import controllers.Game;
import sys.Renderable;
import sys.Renderer;
import ui.Button;

public class ChestScreen implements Renderable {
    public final Game controller;

    private boolean rendered;
    private int layer = 0;
    Vector2f position;
    Vector2i size;

    public final Sprite background = new Sprite();
    //protected Texture[] collectibleTextures = new Texture[5];
    private int chosenHero = 0;

    private Text title;
    private Text subTitle;
    private Collectible[] CollectiblesForChest; //return array of Critters to print
    private Button collectItems;
    private Texture CollectibleTexture;
    private Texture texture = new Texture();

    public ChestScreen(Game game) throws IOException {
        this.controller = game;
        this.background.setTexture(controller.GRAPHICS.HOMESCREEN_BACKGROUND);
        this.background.scale((float) game.window.getSize().x / 2, (float) game.window.getSize().y / 2);

        final Vector2f sizeArea = new Vector2f(500, 500);
        Vector2f winSize = new Vector2f(game.window.getSize());
        final Vector2f positionArea = Vector2f.div(Vector2f.sub(winSize, sizeArea), 2);

        // System.out.println("BEFORE we load the fonts");
        Font ui_font = FontCatalogue.get().FONT_UI_BAR;
        this.title = new Text("Chest", ui_font, 100);
        title.setPosition((controller.window.getSize().x - title.getGlobalBounds().width) / 2,
                positionArea.y + 100);
        title.setColor(new Color(251, 205, 66));
        subTitle = new Text("Congrats! You got: ", ui_font, 50);
        subTitle.setPosition((controller.window.getSize().x - subTitle.getGlobalBounds().width) / 2,
                this.title.getPosition().y + title.getGlobalBounds().height + 50);
    }

    /**
     * call collectiblesForChest method to return an array of collectible paths.
     * we then load the corresponding images and draw them onto the screen
     * @param r
     * @return
     * @throws IOException
     */
    public List<Sprite> returnCollectibleSprites(Renderer r) throws IOException {

        List<String> collectiblePaths;
        List<Sprite> collectibleSprites = null;
        Collectible collectible = null;
        collectiblePaths = collectible.collectiblesForChest(controller.scene, r);
        for(int i =0; i < collectiblePaths.size(); i++) {
            Path collectiblePath = Path.of(collectiblePaths.get(i));
            CollectibleTexture.loadFromFile(collectiblePath); //load texture from file
            Sprite collectibleSprite = new Sprite(CollectibleTexture);
            //make a sprite with that texture
            //this.CollectibleTextures.update(game.window);
            collectibleSprites.add(collectibleSprite);
        }
        return collectibleSprites;
    }

    /**
     * drawing all the elements onto the screen
     * @param arg0
     * @param arg1
     */
    @Override
    public void draw(RenderTarget arg0, RenderStates arg1) {
        background.draw(arg0, arg1);
        this.title.draw(arg0, arg1);
        this.subTitle.draw(arg0, arg1);
        //this.CollectibleTextures.draw(arg0, arg1);
        //this.startButton.draw(arg0, arg1);
        //this.exitButton.draw(arg0, arg1);

    }

    @Override
    public boolean isBeingRendered() {
        return rendered;
    }

    @Override
    public void startRendering(boolean rendered) {
        this.rendered = rendered;
    }

    @Override
    public Sprite getSprite() {
        return background;
    }

    @Override
    public void setTexture(Texture texture) {
        background.setTexture(texture);
    }

    @Override
    public int getLayer() {
        return layer;
    }

    @Override
    public void setLayer(int layer) {
        this.layer = layer;
    }

    @Override
    public void addToRenderer(Renderer renderer) {
        renderer.addToRenderingList(this);
    }

    @Override
    public void removeFromRenderer(Renderer renderer) {
        renderer.removeFromRenderingList(this);
    }

    @Override
    public Vector2f getPosition() {
        return null;
    }

    @Override
    public void setPosition(Vector2f position) {
        this.position = position;

    }

    @Override
    public final void setPosition(float x, float y) {
        this.setPosition(new Vector2f(x, y));
    }

    public void handleEvent(Event event) {
        switch (event.type) {
            case CLOSED:
                this.controller.window.close();
                break;
            case KEY_PRESSED:
                // System.out.println("Key pressed " + event.asKeyEvent().key);
                // this.controller.startGame(null);

                break;
            case MOUSE_BUTTON_PRESSED:
                // System.out.println("Position " + event.asMouseButtonEvent().position);
                if (collectItems.press()) {
                    System.out.println("Collected!");
                    this.controller.window.close();
                }
                break;
            default:
                break;
        }
    }

}
