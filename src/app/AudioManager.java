package app;

import model.structures.AudioWrapper;

import org.lwjgl.openal.AL;
import org.newdawn.slick.openal.SoundStore;

import app.ContentManager.ContentType;

public class AudioManager {
	public static void playSample(String name, float vol) {
		((AudioWrapper) ContentManager
				.get(ContentType.audioSample, name)).play(vol);
	}

	public static void playSample(String name) {
		playSample(name, 100);
	}

	public static void playLoop(String name, float vol) {
		((AudioWrapper) ContentManager.get(ContentType.audioLoop, name))
				.play(vol);
	}

	public static void playLoop(String name) {
		playLoop(name, 100);
	}

	/**
	 * call this once per frame
	 */
	public static void update() {
		SoundStore.get().poll(0);
	}

	/**
	 * call this to clean up
	 */
	public static void finish() {
		AL.destroy();
	}
}
