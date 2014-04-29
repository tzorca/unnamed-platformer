package model.structures;

import org.newdawn.slick.openal.Audio;

/**
 * A handle on a loaded audio object, to play the same sound repeatedly without
 * having to look it up
 */
public class AudioWrapper {

	private Audio sound;
	private AudioType type;

	public AudioWrapper(AudioType t, Audio a) {
		type = t;
		sound = a;
	}

	/**
	 * play this sound at full volume
	 */
	public void play() {
		play(1.0f);

	}

	/**
	 * Play this sound
	 * 
	 * @param vol
	 *            value between 0.0 and 1.0 to specify relative volume of sound
	 */
	public void play(float vol) {
		switch (type) {
		case LOOP:
			sound.playAsMusic(1.0f, vol, true);
			break;

		case SAMPLE:
			sound.playAsSoundEffect(1.0f, vol, false);
			break;
		}
	}

}
