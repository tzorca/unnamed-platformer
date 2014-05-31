package unnamed_platformer.app;

import org.lwjgl.openal.AL;
import org.newdawn.slick.openal.SoundStore;

import unnamed_platformer.game.parameters.ContentRef.ContentType;
import unnamed_platformer.game.structures.AudioWrapper;

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
