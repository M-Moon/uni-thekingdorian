package ui;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import javax.swing.text.Position;
import java.io.IOException;
import java.nio.file.Paths;

public class SpikeWall implements Runnable, Drawable {

    public class Coin implements Runnable, Drawable {
        private Texture spriteSheet = assets.TextureCatalogue.get().COIN_SHEET;
        Sprite coin;
        private IntRect spriteRect = new IntRect(0, 0, 160, 160);
        private int frame = 0;

        public Coin(Vector2f position, float scale) {
            // try
            // {
            // spriteSheet.loadFromFile(Paths.get("resources/UI/CoinSpriteSheet.png"));
            // } catch (IOException e)
            // {
            // try
            // {
            // spriteSheet.loadFromFile(Paths.get("Game/resources/UI/CoinSpriteSheet.png"));
            // } catch (IOException e2)
            // {
            // System.out.println("[ERROR] Could not load coin images.");
            // e2.printStackTrace();
            // }
            // }

            coin = new Sprite(spriteSheet, spriteRect);
            coin.setScale(scale, scale);
            coin.setOrigin(80, 80);
            coin.setPosition(position);
        }

        public void run() {
            while (frame < 8) {
                coin.setTextureRect(new IntRect((frame * 160), 0, 160, 160));
                frame++;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            frame = 0;
            coin.setTextureRect(new IntRect((frame * 160), 0, 160, 160));
            return;
        }

        /*
         * public void draw(RenderWindow window){ window.draw(coin); }
         */

        @Override
        public void draw(RenderTarget renderTarget, RenderStates renderStates) {
            coin.draw(renderTarget, renderStates);
        }

    }

    @Override
    public void draw(RenderTarget arg0, RenderStates arg1) {
        // TODO Auto-generated method stub

    }

    @Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}