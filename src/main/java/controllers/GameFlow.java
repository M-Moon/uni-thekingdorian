package controllers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import ManageAnimation.PopupAnimation;
import ManageAnimation.Transition;
import assets.FontCatalogue;
import assets.TextureCatalogue;
import devtools.*;
import managers.collectiblemanagers.*;
import managers.crittermanagers.*;
import managers.crittermanagers.EnemyManager;
import managers.crittermanagers.GuideManager;
import managers.crittermanagers.ObstacleManager;
import managers.crittermanagers.TileManager;
import platformer.GameScene;
import platformer.GameScene.SceneType;
import ui.PopupFrame;
import ui.TextBox;


/**
 * A class which monitors the actual gameplay.
 * <p>
 * This class periodically checks in with various game elements
 * and ultimates decides what new elements should be spawned, e.g., should we start spawning harder enemies?
 * This is also the class which controls the different overlays
 * 
 */
public class GameFlow implements Observer
{
    public final Game controller;

    /**
     * The current chapter that the player is at.
     * This is simply for storytelling purposes.
     * After the key is spawned (Chapter 7), there isn't anymore chapters
     * (technically, it is completed when the player wins or loses).
     * 
     * <p>
     * Chapter 0 is the beginning 
     */
    private int chapter = 0;
    /**
     * When the last chapter had ended
     */
    private float chapterTimer = 0;   
    
    /**
     * Storyline
     */
    private List<Sequence> storyBook = new ArrayList<>();

    /**
     * List of popups to put up on the screen.
     * When the list has transitions, we show them
     */
    private Queue<Transition> popups = new LinkedList<>();
    /** 
     * Whether there currently something being showed on screen
     */
    private boolean running = false;
    

    public GameFlow(Game game) {
        this.controller = game;
        this.controller.scene.addObserver(this);

        // Create the transition (pop up)

        this.storyBook.add(new Sequence("Player",0,1) {
            void next() {
                // When the game starts, authorize the coins
                TileManager.get(game).setConstraints(1,4, 2f,3f);
                CoinManager.get(game).setConstraints(1,4,3f,7f);
                ObstacleManager.get(controller).setConstraints(1, 2, 4f, 6f);
                PotionsManager.get(controller).setConstraints(1, 3, 2, 6f);
                // Tip: pause
                String tip = "Need a break? Press 'P' to pause the game";
                addTransition(new Tip(tip, null).createTransition(10f));
            }
        });

        this.storyBook.add(new Sequence("Explorer",15,20) {
            @Override
            void next() {
                // Increase coin value by 10%
                CoinManager.get(controller).setCoinValueMarkup(0.1f);
                // Start spawning potions, obstacles and weapons
                ObstacleManager.get(controller).setConstraints(1, 2, 4f, 6f);
                WeaponryManager.get(controller).setConstraints(1,2, 2f,7f);
                // Show a tip: Avoid the obstacles Press H to heal from your injuries, image potions
                String tip = "Avoid the obstacles to save your strength and use your potions by pressing H.";
                addTransition(new Tip(tip, game.GRAPHICS.POTION_TYPE_1).createTransition(10f));
            }
        });
        this.storyBook.add(new Sequence("Runner",15,30){
            @Override
            void next() {
                // Start spawning enemies
                EnemyManager.get(controller).setConstraints(1, 3, 2, 5);
                // Spawn defense potions more often
                CoinManager.get(controller).setTimeBetweenSpawnsLimits(2F, 5f);
                PotionsManager.get(controller).setConstraints(1, 5, 1f, 5f);
                ObstacleManager.get(controller).setConstraints(1, 3, 5f, 6f);
                WeaponryManager.get(controller).setConstraints(1,2, 2f,7f);
                // Show a tip: Press SPACE to attack the enemies
                String tip = "Attack the enemies by pressing the 'SPACE' bar.";
                addTransition(new Tip(tip, null).createTransition(10f));
            }
        });
        this.storyBook.add(new Sequence("Fighter",10f,20f){
            @Override
            void next() {
                // At the end of the fighter chapter, give more coins (+0.08)
                CoinManager.get(game).setCoinValueMarkup(0.08f);
                CoinManager.get(game).setConstraints(2, 5, 3f, 8f);
                // Start spawning the guide
                GuideManager.get(controller).setConstraints(1, 2, 20f, 30f);
                // Show a tip: The Crossroads guide can help you travel to the other realms: Press I when you're near it
                String tip = "The Crossroads guide can help you travel to the other realms; press 'I' when you're near them";
                addTransition(new Tip(tip, game.GRAPHICS.NPC_GUIDE_ZONE).createTransition(10f));
            }
        });
        this.storyBook.add(new Sequence("Traveller",20f,35f){
            @Override
            void next() {
                // At the end of the Traveller chapter, more enemies and weapons
                WeaponryManager.get(controller).setConstraints(1,2, 2f,7f);
                EnemyManager.get(controller).setConstraints(1, 3, 5, 7);
                PotionsManager.get(controller).setConstraints(0, 5, 1f, 5f);
                // Reduce guide spawning
                GuideManager.get(controller).setConstraints(0, 2, 20f, 30f);
            }
        });
        this.storyBook.add(new Sequence("Settler",30f,35f){
            @Override
            void next() {
                // Settler chapter is just a time where nothing new happens. At the end,
                // Start spawning the merchant
                MerchantManager.get(controller).setConstraints(1,2, 20f,30f);
                // Give more coins (+0.1)
                CoinManager.get(game).setCoinValueMarkup(0.1f);
                // Scroll a little faster (+40%)
                // controller.scene.setScrollingSpeed(Vector2f.mul(controller.scene.getScrollingSpeed(), 1.4f));
                WeaponryManager.get(controller).setConstraints(0,4, 2f,5f);
                EnemyManager.get(controller).setConstraints(1, 3, 2f, 5f);
                ObstacleManager.get(game).setConstraints(1,4,2f,5f);
                PotionsManager.get(controller).setConstraints(0, 5, 1f, 5f);
                // Reduce guide spawning
                GuideManager.get(controller).setConstraints(0, 2, 20f, 30f);
            }
        });
        this.storyBook.add(new Sequence("Trader",35f,40f){
            @Override
            void next() {
                // Settler chapter is just a time where nothing new happens. At the end,
                // Start spawning the merchant - they may have the key
                MerchantManager.get(controller).setConstraints(1,2, 20f,25f);
                // Give more coins (+0.1)
                CoinManager.get(game).setCoinValueMarkup(0.1f);
                // Scroll a little faster (+40%)
                controller.scene.setScrollingSpeed(Vector2f.mul(controller.scene.getScrollingSpeed(), 1.4f));
                // More obstacles
                ObstacleManager.get(game).setConstraints(0,4,3f,7f);
                // Tip: Watch out for the merchant they may have interesting trinkets for you ... for a price! (lasts 15s)
                String tip = "Watch out for the merchant: they may have interesting trinkets for you ... for a price!";
                addTransition(new Tip(tip, game.GRAPHICS.NPC_GUIDE_ZONE).createTransition(10f)); 
            }
        });
        // Last technical chapter: 
        this.storyBook.add(new Sequence("Saviour",30f,35f){
            @Override
            void next() {
                // Authorize the key
                KeyManager.get(game).setConstraints(1, 2, 35f, 40f);
                CoinManager.get(game).setCoinValueMarkup(0.12f);
                WeaponryManager.get(controller).setConstraints(0,4, 2f,5f);
                EnemyManager.get(controller).setConstraints(1, 3, 5, 7);
                PotionsManager.get(controller).setConstraints(0, 5, 1f, 5f);
                // Reduce guide spawning
                GuideManager.get(controller).setConstraints(0, 2, 15f, 20f);
                // Tip: Remember to collect the key when you see it!
                String tip = "Remember to collect the key when you see it!";
                addTransition(new Tip(tip, game.GRAPHICS.KEY).createTransition(10f)); 
            
            }
        });

        System.out.println("Minimum game time: " + storyBook.stream().mapToDouble(s->s.length).sum() +" sec.");

        

        // Start:
        this.chapter = 0;
        this.chapterTimer = 0f;

    }

    /**
     * Main method of the GameFlow class. It checks the player's 
     * progress against the storyline and the spaning of critters.
     */
    public void checkProgress(float time) {
        
        // Check the transition states 
        if (running) {
            this.popups.peek().apply();
        } else {
            if (!this.popups.isEmpty()) {
                this.running = true;
                this.controller.gameRenderer.add(this.popups.peek());
                this.popups.peek().start();
            } 
        }

        // Check that progress was made
        float chapterLength = time - this.chapterTimer;
        if (this.chapter >= this.storyBook.size()) {
            // Nothing
        } else if (this.chapter == 0) {
            this.next(time);                // Go straight to chapter 1

        } else if (this.storyBook.get(chapter).isCompleted(chapterLength)) {
            this.next(time);                // Go to the next chapter
        }
    }

    /**
     * Starts the next chapter.
     * @param time time since game started
     */
    private void next(float time) {
        if (this.chapter == this.storyBook.size()) {
            // Game is won
            return;
        }

        this.storyBook.get(chapter).next();
        System.out.println("Chapter " + chapter + " '" + storyBook.get(chapter).chapter + "' ending after " +  storyBook.get(chapter).length);
        this.chapter ++;            // New chapter
        this.chapterTimer = time;   // Just startin
        // this.currentSequence = gameSequences.get(this.currentSequence.index + 1);
        // System.out.println("Chapter " + chapter + " - " + storyBook.get(chapter).chapter);
    }

    public void addTransition(Transition transition) {
        // Remove it from this list when it's done
        transition.addObserver(evt -> {
            this.popups.remove(transition);
            controller.gameRenderer.remove(transition);
            this.running = false;
        });
        this.popups.add(transition);
        
	}



    public void receiveUpdate(ObservableEvent event) {

        // new transition: apply == if time<TipLim transition completed
        
        // if (event.getEventID()==GameScene.EVENT_SCENE_CHANGE) {
        //     Guide guide = (Guide) event.getSource();
        //     SceneType newType = guide.getDestinationScene();
        //     FloatRect sceneArea = new FloatRect(controller.scene.getPosition(), new Vector2f(controller.scene.getSceneSize()));
        //     this.transitionState = new Transition(sceneArea, 20) {
        //         @Override
        //         public void applyEffect() {
        //             // controller.scene.setSceneBackground();
        //             // Empty the active list
        //             controller.scene.setSceneType(newType);
        //             controller.critterManager.resetScene(guide);
        //         }
        //     }; 
        //     this.controller.gameRenderer.add(transitionState);
        //     this.transitionState.addObserver(evt -> {
        //         if (evt.getEventID()==Transition.EVENT_TRANSITION_COMPLETED) {
        //             controller.gameRenderer.remove(transitionState);
        //             this.transitionState = null;
        //             this.controller.setClockState(true);
                    
        //         }
        //     });
        //     this.controller.setClockState(false);
        //     this.transitionState.start();
        // }
    }

    /**
     * Puts the game flow at the very start of the game.
     */
    public void restart() {
        this.chapter = 0;
        this.chapterTimer = 0f;
        this.storyBook.forEach(Sequence::resetSequence);

    }

    protected abstract static class Sequence {

        /** The length of a sequence */
        private float length;
        /** The approximate length of this Sequence */
        private float minChapterLength;
        private float maxChapterLength;
        /** The name of a sequence */
        private String chapter;
        /**
         * Creates a new chapter for the game.
         * 
         * @param name
         * @param minTime
         * @param maxTime
         */
        Sequence(String name, float minTime, float maxTime) {
            this.chapter = name;
            this.minChapterLength = minTime;
            this.maxChapterLength = maxTime;

            this.resetSequence();
            System.out.println("Chapter: " + chapter + " lasts " + length + " seconds");
        }

        /**
         * Resets the duration of a chapter of the game
         */
        void resetSequence() {
            this.length = (float) Math.random() * (maxChapterLength-minChapterLength) + minChapterLength;
        }

        /**
         * Determines whether this time-dependent sequence is over
         * 
         * @param time the time since this chapter started
         * @return true only if the 
         */
        boolean isCompleted(float time) {
            return time >= length;
        }
        /**
         * Defines the actions to be undertaken when this chapter ends.
         * 
         */
        abstract void next();
    }

    private static final class Tip extends PopupFrame {
        private TextBox text;
        private Sprite icon;

        Tip(String text, Texture textureIcon) {
            super(Tip.DEFAULT_POPUP_SIZE, Tip.DEFAULT_POPUP_POSITION);
            this.setTitle("Tip", null, 25);
            this.setBackground(TextureCatalogue.get().HOMESCREEN_BACKGROUND);

            // Add the tip of the text
            this.text = new TextBox(text, FontCatalogue.get().FONT_FREESANS, 20, new Vector2f(340,70));
            this.text.setPosition(mapPanelToWindow(new Vector2f(10,45)));
            this.addComponent(this.text);

            // Add the icon - right side
            this.icon = new Sprite();
            if (textureIcon!=null) {
                this.icon.setTexture(textureIcon);
                this.icon.scale(80/icon.getGlobalBounds().width, 80/icon.getGlobalBounds().height);
                this.icon.setPosition(mapPanelToWindow(new Vector2f(330,40)));
            }
            this.addComponent(icon);
        }

        PopupAnimation createTransition(float time) {
            PopupAnimation animation = new PopupAnimation(this, time);
            return animation;
        }
    }

    public int getChapter(){
        return chapter;
    }
}
