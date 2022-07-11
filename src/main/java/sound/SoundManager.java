package sound;

import assets.SoundCatalogue;
import org.jsfml.audio.Music;
import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for managing the sounds within the game.
 * This can be loaded by anything that needs to play sound, or loaded by an overhead manager class that plays sounds
 * after certain events are triggered.
 *
 * This class works in tandem with the SoundCatalogue class, a singleton which loads all the sound assets for ease-of-use.
 *
 * Only 32 channels are available for playing sounds all at once with the jsfml library, so care must be taken to make sure
 * the total number of sounds being played within a program does not exceed 32 in any one given moment.
 *
 * SoundManagers should be separated between sfx and music. Due to how the arraylist that holds the 32 total gets cut down
 * by removing the first 29 items (leaving the 3 most recent sounds, with a 4th then added on top), meaning music which is
 * playing - and has most likely been instantiated near the beginning of the list - will be instantly dropped and lost once the list
 * reaches capacity.
 *
 * @author JoeMoon
 */
public class SoundManager
{
    private Sound sound;
    private List<Sound> soundArrayList = new ArrayList<>(32);
    private boolean soundSet;

    private Music musicStream;
    private boolean musicSet;

    /**
     * Constructor.
     * The sound is instantiated here so that once the playSound() method ends, the sound does not suddenly cut off due to
     * the Sound variable getting garbage collected by the JVM.
     */
    public SoundManager()
    {
        this.sound = new Sound();
        this.musicStream = new Music();
        this.soundSet = false;
        this.musicSet = false;
    }

    /**
     * Method for setting the volume of this sound manager, affecting all sounds that are played
     *
     * @param volume float, the volume to play sounds at - (100 is full volume, 0 is silent)
     */
    public void setSoundVolume(float volume)
    {
        sound.setVolume(volume);
    }

    public void setMusicVolume(float volume)
    {
        musicStream.setVolume(volume);
    }

    /**
     * Method for adding to the list that holds the sounds, a total of 32 for the 32 jsfml channels.
     * Once capacity is reached, the last 4 elements (Which are most likely to still be playing) are sliced from
     * the list and put into a temporary list. The main list is then made to hold those 16 elements, freeing up the
     * last 28 spaces for more sounds.
     * <p>
     * A new soundForList is created from the given sound so that when multiple sounds get played, the sound
     * in the original sound variable doesn't stop playing.
     *
     * @param sound Sound to be added to the list
     */
    private void addToSoundList(Sound sound)
    {
        Sound soundForList = new Sound(sound);
        if (soundArrayList.size() >= 32)
        {
            List<Sound> tmpList = soundArrayList.subList(29, 32);
            //System.out.println("size of tmplist = " + tmpList.size());
            soundArrayList = tmpList;
        }
        soundArrayList.add(soundForList);
        //System.out.println("Array list size: " + soundArrayList.size());
    }

    /**
     * Used to play sounds in the program.
     * Sets the soundBuffer of sound to the relevant sound as indicated by the given string.
     *
     * Every time a sound is added to the SoundCatalogue, it must be added here too. Somewhat inefficient, but easy.
     *
     * The method checks if soundSet is true, which it's set to when a sound is successfully loaded in the buffer, and then plays the sound.
     * soundSet is then set back to false, meaning if the method is called again with an invalid name, the previously set sound
     * isn't just played again.
     *
     * @param soundName the name of the sound to be played, often corresponding to a lower case version of the name in SoundCatalogue
     */ 
    public void playSound(String soundName)
    {
        if (soundName.equals("hit_1"))
        {
            sound.setBuffer(SoundCatalogue.get().HIT_1);
            soundSet = true;
        } else if (soundName.equals("hit_2"))
        {
            sound.setBuffer(SoundCatalogue.get().HIT_2);
            soundSet = true;
        } else if (soundName.equals("coin_pickup_1"))
        {
            sound.setBuffer(SoundCatalogue.get().COIN_PICKUP_1);
            soundSet = true;
        } else if (soundName.equals("coin_pickup_2"))
        {
            sound.setBuffer(SoundCatalogue.get().COIN_PICKUP_2);
            soundSet = true;
        } else if (soundName.equals("jump_1"))
        {
            sound.setBuffer(SoundCatalogue.get().JUMP_1);
            soundSet = true;
        } else if (soundName.equals("potion_pickup_1"))
        {
            sound.setBuffer(SoundCatalogue.get().POTION_PICKUP_1);
            soundSet = true;
        } else if (soundName.equals("success_1"))
        {
            sound.setBuffer(SoundCatalogue.get().SUCCESS_1);
            soundSet = true;
        }

        if (soundSet)
        {
            addToSoundList(sound);
            soundArrayList.get(soundArrayList.size() - 1).play();
            soundSet = false;

            //System.out.println("Sound played: " + sound.getBuffer());
        }
    }

    /**
     * Same as for playSound, but specifying music to be played.
     * Keeps things more organised + explicit, but essentially redundant.
     * However the jsfml wiki recommends using soundStream for music (so it isn't cut off by excess use of the buffer),
     * thus JUSTIFYING THIS MF EXTRA METHOD. skrr.
     *
     * @param soundName the name of the sound to be played, often corresponding to a lower case version of the name in SoundCatalogue
     */
    public void playMusic(String soundName)
    {
        try
        {
            if (soundName.equals("marcelo_fernandez_anewheroisrising_intromusic.wav")) {
                musicStream.openFromFile(SoundCatalogue.get().INTRO_1);
                musicSet = true;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }


        if (musicSet)
        {
            musicStream.play();
            System.out.println("Music details: " + musicStream.getStatus() + " " + musicStream.getVolume());
            musicSet = false;
        }
    }

    /**
     * Stops playing the sound.
     */
    public void stopPlayingSound()
    {
        sound.stop();
    }

    public void stopPlayingMusic()
    {
        musicStream.stop();
    }
    
    /**
     * Method for playing the sound last loaded by the class
     */
    public void playLastSound()
    {
        soundArrayList.get(soundArrayList.size()-1).play();
    }
}
