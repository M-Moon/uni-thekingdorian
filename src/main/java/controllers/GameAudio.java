package controllers;

import org.jsfml.audio.*;
import sound.*;

/**
 * A class which takes care of playing appropriate sounds during the gameplay.
 * <p> This class is essentially 
 * 
 */
public final class GameAudio 
{
    /**
     * The current sound effect being played
     */
    private SoundManager soundEffect;

    /**
     * The current music being played
     */
    private SoundManager ambientMusic;     
    
    public GameAudio() 
    {
        soundEffect = new SoundManager();
        ambientMusic = new SoundManager();
    }

    /**
     * Plays the given sound effect
     * 
     * @param effectName the name of the effect, as described in SoundManager
     * @see SoundManager
     */
    public void playEffect(String effectName)
    {
        this.soundEffect.playSound(effectName);
    }

    /**
     * Plays the given music
     * 
     * @param musicName the name of the music, as described in SoundManager
     * @see SoundManager
     */
    public void playMusic(String musicName) 
    {
        this.ambientMusic.playMusic(musicName);
    }

    public void stopMusic() 
    {
        this.ambientMusic.stopPlayingMusic();
    }

    public void stopEffect() 
    {
        this.soundEffect.stopPlayingSound();
    }

    public void setEffectVolume(float volume)
    {
        soundEffect.setSoundVolume(volume);
    }

    public void setMusicVolume(float volume)
    {
        ambientMusic.setMusicVolume(volume);
    }
}
