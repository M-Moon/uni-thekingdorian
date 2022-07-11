package fighter;

import models.Critter;

import java.util.Random;

import org.jsfml.system.Vector2f;

import Collectibles.weaponCollectible;
import platformer.AbstractScene;
import platformer.Platformer;

public class Enemy extends Critter implements Damageable {

    // Health system
    protected int HEALTH_MAX = 3; // see weaponCollectible
    protected int healthPoints;


    @Override
    public void die() {
        // TODO Auto-generated method stub
        // System.out.println("Enemy will fall, turn into smoke and sprite will
        // disappear");

    }

    @Override
    public void takeDamage(int damage) {
        healthPoints--;
    }

    @Override
    public void inflictDamage(int damage, Damageable adversary) {
        adversary.takeDamage(damage);

    }

    @Override
    public boolean isDead() {
        return healthPoints == 0;
    }

    @Override
    public boolean isHealthFull() {
        return healthPoints == HEALTH_MAX;
    }

    @Override
    public int getHealth() {
        return this.healthPoints;
    }

    @Override
    public void placeInScene(AbstractScene scene, Vector2f availablePosition ) {
        // TODO Implement his abstract method to place this token on the scene

    }

    @Override
    public void place(Platformer scene, Vector2f availablePosition) {
        //
    }

    
    /** 
     * it return a true or flase to indice the damage of the bow or sword 
     * @param value the damage value 
     * @return boolean
     */
    public boolean fight(int value) {
        if (value < this.healthPoints) return false;

        if (this.healthPoints == weaponCollectible.WEAPON_STRENGTH_BOW) {
            return new Random().nextInt(100) < 90;
        }

        else if (this.healthPoints == weaponCollectible.WEAPON_STRENGTH_SWORD) {
            return new Random().nextInt(100) < 70;
        }
        
        return false;
    }
    
    public int getStrength() {
        return healthPoints;
    }

    @Override
    public CritterType getCritterType() {
        return CritterType.ENEMY;
    }
    
}
