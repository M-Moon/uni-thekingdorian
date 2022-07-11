import java.awt.event.*; 
import java.awt.*; 
import javax.swing.*; 
import java.lang.Object;
import org.jsfml.graphics;

/* Brief: All game tokens must be implemented, and renderable on the screen. 
That includes coins, key, health consumables, basic weapons. 
Game tokens are physical game objects which can be collected (implements a Collectible interface) 
*/

public class gameTokens extends JFrame 
{
    private static final long serialVersionUID = 1L;
    static JFrame frame ; 
  
    /**
    Removes token from JFrame (used when Player's hitbox interacts with Object)
    * @param  JLabel of an object
    */
    static void removeToken(JLabel j)
    {
        j.setVisible(false);
        String tokenName = j.getName();
        //add tokenName to inventory
    }

    /**
    Sets a hitbox for the given JLabel and uses intersection method to detect if player has interacted with hitbox. if so, remove token and add to inventory 
    * @param  JLabel of an object
    */
    static void setHitBox(JLabel j)
    {   
        //String hitBoxName = j.getName() + "hitBox";
        //System.out.println(hitBoxName);
        FloatRect hitBox = new FloatRect(j.getLocation(), j.getSize()); //making a floatRect around a given JLabel
        if(hitBox.intersection()) //put player.hitbox in these brackets once the player sprite has been created
        {
            removeToken(j);
        }
    }

    
    public static void main(String[] args) 
    { 
        //set images as JLabels and create hitboxes around them 
        JLabel coin = new JLabel("coin");
        coin.setIcon(new ImageIcon("E:\\coin.png"));
        coin.setName("coin");
        setHitBox(coin);

        JLabel key = new JLabel("key");
        key.setIcon(new ImageIcon("E:\\key.png"));
        key.setName("key");
        setHitBox(key);

        JLabel potion1 = new JLabel("potion2");
        potion1.setIcon(new ImageIcon("E:\\potion2.png"));
        potion1.setName("potion1");
        setHitBox(potion1);

        JLabel potion2 = new JLabel("potion2");
        potion2.setIcon(new ImageIcon("E:\\potion2.png"));
        potion2.setName("potion2");
        setHitBox(potion2);

        JLabel sword = new JLabel("sword");
        sword.setIcon(new ImageIcon("E:\\sword.png"));
        sword.setName("sword");
        setHitBox(sword);

        JLabel bowAndArrow = new JLabel("bowAndArrow");
        bowAndArrow.setIcon(new ImageIcon("E:\\bowAndArrow.png"));
        bowAndArrow.setName("bowAndArrow");
        setHitBox(bowAndArrow);

        //place JLabel on game map (made this a blank panel for testing purposes)
        JPanel p = new JPanel();
        p.add(coin);
        p.add(key);
        p.add(potion1);
        p.add(potion2);
        p.add(sword);
        p.add(bowAndArrow); 
    
        frame.setSize(300, 300); 
        frame.setVisible(true); 
       
    }
}

