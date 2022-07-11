package fighter;

public interface Damageable 
{
    public void die();
    
    public void takeDamage(int damage);

    public void inflictDamage(int damage, Damageable adversary);

    public int getHealth();

    public boolean isDead();

    public boolean isHealthFull();
}
