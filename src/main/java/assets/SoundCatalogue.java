package assets;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.audio.SoundStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The sound catalogue, a singleton that stores all the asset sounds for use throughout the program.
 * Works in tandem with SoundManager, should not be accessed directly.
 *
 * @author JoeMoon, based on Lydie's FontCatalogue
 */
public final class SoundCatalogue
{
    private static SoundCatalogue INSTANCE = null;
    private static boolean initialised = false;

    public static final SoundCatalogue get() {
        if (!initialised)
        {
            INSTANCE = new SoundCatalogue();
            SoundCatalogue.initialised = true;
        }
        return INSTANCE;
    }

    // sfx
    public final SoundBuffer JUMP_1 = new SoundBuffer();

    public final SoundBuffer HIT_1 = new SoundBuffer();
    public final SoundBuffer HIT_2 = new SoundBuffer();

    public final SoundBuffer COIN_PICKUP_1 = new SoundBuffer();
    public final SoundBuffer COIN_PICKUP_2 = new SoundBuffer();

    public final SoundBuffer POTION_PICKUP_1 = new SoundBuffer();

    public final SoundBuffer SUCCESS_1 = new SoundBuffer();

    // music (needs to be a path, can't be final)
    public Path INTRO_1;

    /**
     * Initialises all the sounds.
     * Uses 2 try-catch blocks, one within the other, to account for the two paths that seem to differ by system.
     */
    private SoundCatalogue()
    {
        try
        {
            JUMP_1.loadFromFile(Paths.get("resources/sounds/sfx/jump/RetroJumpClassic08.wav"));

            HIT_1.loadFromFile(Paths.get("resources/sounds/sfx/impact_noises/RetroImpactPunch01.wav"));
            HIT_2.loadFromFile(Paths.get("resources/sounds/sfx/impact_noises/RetroImpactPunch07.wav"));

            COIN_PICKUP_1.loadFromFile(Paths.get("resources/sounds/sfx/coins/RetroPickUpCoin04.wav"));
            COIN_PICKUP_2.loadFromFile(Paths.get("resources/sounds/sfx/coins/RetroPickUpCoin07.wav"));

            POTION_PICKUP_1.loadFromFile(Paths.get("resources/sounds/sfx/potions/RetroPotionSound1-new.wav"));

            SUCCESS_1.loadFromFile(Paths.get("resources/sounds/music/success_music/RetroSuccessMelody01.wav"));

            INTRO_1 = Paths.get("resources/sounds/music/marcelo_fernandez_anewheroisrising_intromusic.wav");
        } catch (IOException e)
        {
            try
            {
                JUMP_1.loadFromFile(Paths.get("Game/resources/sounds/sfx/jump/RetroJumpClassic08.wav"));

                HIT_1.loadFromFile(Paths.get("Game/resources/sounds/sfx/impact_noises/RetroImpactPunch01.wav"));
                HIT_2.loadFromFile(Paths.get("Game/resources/sounds/sfx/impact_noises/RetroImpactPunch07.wav"));

                COIN_PICKUP_1.loadFromFile(Paths.get("Game/resources/sounds/sfx/coins/RetroPickUpCoin04.wav"));
                COIN_PICKUP_2.loadFromFile(Paths.get("Game/resources/sounds/sfx/coins/RetroPickUpCoin07.wav"));

                POTION_PICKUP_1.loadFromFile(Paths.get("Game/resources/sounds/sfx/potions/RetroPotionSound1-new.wav"));

                SUCCESS_1.loadFromFile(Paths.get("Game/resources/sounds/music/success_music/RetroSuccessMelody01.wav"));

                INTRO_1 = Paths.get("Game/resources/sounds/music/marcelo_fernandez_anewheroisrising_intromusic.wav");

                return;
            } catch (IOException e2)
            {
                System.out.println("[ERROR] Could not load sounds");
                e2.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
