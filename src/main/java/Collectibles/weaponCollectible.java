package Collectibles;

import hero.Hero;
import managers.collectibleManager;
import models.Critter;
import org.jsfml.system.Vector2f;
import platformer.GameScene;
import sys.Renderer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import controllers.Game;
import fighter.Enemy;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;

public class weaponCollectible extends Collectible {

    public static final int WEAPON_TYPE_BOW = 0;
    public static final int WEAPON_TYPE_SWORD = 1;

    public static final int WEAPON_STRENGTH_BOW = 3;
    public static final int WEAPON_STRENGTH_SWORD = 5;

    public static final String PATH_STRING_WEAPON_BOW = "resources/placeholder/bowAndArrow.png";
    public static final String PATH_STRING_WEAPON_SWORD = "resources/placeholder/sword.png";


    private static final Object Collectible = null;
    Critter bowAndArrow = createCollectible(Path.of("/resources/placeholder/bowAndArrow.png"));
    Critter sword = createCollectible(Path.of("/resources/placeholder/sword.png"));
    private Object Critter;

    private Critter createCollectible(Path of) {
        return (models.Critter) Critter;
    }

    int weaponDamage = 0;
    //int swordDamage = 30;
    //int bowDamage = 50;

    int weaponsCount = 0;

    public weaponCollectible(GameScene scene, Renderer renderer) throws IOException {
        super(scene.controller);

        Critter weapon = null;
        int levelNumber = collectibleManager.levelSceneToNumber(scene);
        int limit = levelNumber *1;
        int numberOfWeapons = randomInt(limit);
        List<String> weaponList = Arrays.asList("/resources/placeholder/textures/bowAndArrow.png", "/resources/UI/Sword.png");

        for(int i = 0; i< numberOfWeapons; i++){
            //randomise which potion it is
            Random randomizeCollectible = new Random();
            int randomPotion = randomizeCollectible.nextInt(weaponList.size());
            String randomWeaponPath = weaponList.get(randomPotion);
            weapon = createCollectible(Path.of(randomWeaponPath), renderer);
            //Collectible.animateCollectible(weapon, 1, 8);
            // Set the position of the weapons
            int xCoordinate = 0; //ground level
            int yCoordinate = randomInt(500); //set limit as screen length- is this 500?
            weapon.setPosition(xCoordinate, yCoordinate);
            }
            //return weapon;
        }

    public weaponCollectible(Game controller) {
        this(controller,new Random().nextInt(2));
    }

    public weaponCollectible(Game controller, int weaponType) {
        super(controller);
        this.setWeaponType(weaponType);
        this.setSize(new Vector2f(50,50));
    }

    /**
     * Set the type of weapon, and automatically assigns it the appropriate texture and value.
     * 
     * @param type the integer identifier described in the static field of the class
     */
    private void setWeaponType(int type) {
        if (type>1) return;
        String path = type==WEAPON_TYPE_BOW ? PATH_STRING_WEAPON_BOW : PATH_STRING_WEAPON_SWORD;
        int damage = type==WEAPON_TYPE_BOW ? WEAPON_STRENGTH_BOW : WEAPON_STRENGTH_SWORD;
        this.setTexture(Path.of(path));
        this.setValue(damage);
        
    }

    @Override
    public void tick(float time, Game controller) {
        super.tick(time, controller);

        //System.out.println("Weapon");

        // If it is in the inventory of the player, just move it
        // and check for collision
        if (this.inInventory) {
            // When adding to the renderer, check for this 
            System.out.println("I move, looking for an enemy to hit");
            boolean hit = false;
            for (Enemy enemy : controller.critterManager.getActiveEnemies(this.platformLevel)) {
                if (this.collides(enemy, controller)) {
                    boolean damaged = enemy.fight(this.value);
                    if (damaged) {
                        this.removeFromRenderer(controller.gameRenderer);
                        enemy.removeFromRenderer(controller.gameRenderer);
                        // Release a chest
                        hit = true;
                        break;
                    } 
                }
            }
            if (!hit) System.out.println("Yeay");
        }
        // Otherwise it is just a collectible
        else {
            this.handleCollision(controller, ()->{
                controller.hero.collect(CritterType.WEAPON, value);
                // System.out.println("Weapons count " + controller.hero.getInventoryOf(CritterType.WEAPON));
                this.removeFromRenderer(controller.gameRenderer);
                this.isColliding = false;
            });
        }
    }

    public boolean isBeingLaunched() {
        return inInventory;
    }

    /**
     * Launches a weapon
     */
    public void launchWeapon() {
        // if (controller.hero.spend(WEAPON, ))
        this.inInventory = true;
    }

    public static int getWeaponStrength(int type) {
        int strength = 0;
        if (type == WEAPON_TYPE_BOW) {
            strength = 30;
        }
        else if (type == WEAPON_TYPE_SWORD) {
            strength = 50;
        }
        return strength;
    }

    /**
     * When the player collides with a weapon, set it to move with the hero
     *
     * @param hero - player which collides with weapons
     * @param weapon - weapon that's been collided with
     * @return weaponsCount - the number of weapons collected by the player to be placed on scoreboard
     */
    public int weaponCollision(Hero hero, Critter weapon) {
        if (collectibleCollision(hero, sword) == true)
        {
            //enemyHealth = enemyHealth - swordDamage
            moveCollectibleToInventory(sword);
            Vector2f weaponPosition = hero.getPosition();
            weapon.setPosition(weaponPosition);
            //continuously render using a while(true)?
        }

        else if (collectibleCollision(hero, bowAndArrow) == true){
            moveCollectibleToInventory(bowAndArrow);
            //enemyHealth = enemyHealth - bowDamage
        }
        weaponsCount = weaponsCount +1;
        return weaponsCount;
    }

}