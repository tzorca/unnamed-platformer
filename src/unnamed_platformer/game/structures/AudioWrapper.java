package unnamed_platformer.game.structures;

import org.newdawn.slick.openal.Audio;

import unnamed_platformer.game.parameters.AudioRef.AudioType;

public class AudioWrapper {

	private Audio sound;
	private AudioType type;

	public AudioWrapper(AudioType t, Audio a) {
		type = t;
		sound = a;
	}

	public void play() {
		play(1.0f);

	}

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
