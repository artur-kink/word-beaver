package com.merccoder.wordbeaver;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Audio playing class. Loads and plays audio files.
 */
public class AudioPlayer {

	/** Program context. */
	public static Context context;

	/** Sound pool containing and playing audio files. */
	private static SoundPool soundPool;

	public static boolean soundOn;
	
	public static int tada;
	public static int chomp;

	/**
	 * Initializes audio and sound player.
	 * Must be called before AudioPlayer can be used.
	 */
	public static void initSounds() {
		soundOn = true;
	    soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
	    
	    //Load sounds
	    tada = soundPool.load(context, R.raw.tada, 1);
	    chomp = soundPool.load(context, R.raw.chomp, 1);
	}

	public static void playSound(int id) {
		if(soundOn){
		    AudioManager mgr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);    
		    float volume = streamVolumeCurrent / streamVolumeMax;
	
		    //Play sound.
		    soundPool.play(id, volume, volume, 1, 0, 1f);
		}
	}
}