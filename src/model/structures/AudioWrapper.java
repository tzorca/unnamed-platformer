package model.structures;

import model.parameters.AudioRef.AudioType;

import org.newdawn.slick.openal.Audio;

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
