package unnamed_platformer.res_mgt;

import org.lwjgl.openal.AL;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.SoundStore;

public class SoundManager {
	public static void playSample(String name) {
		ResManager.get(Audio.class, name).playAsSoundEffect(1.0f, 1.0f, false);
	}

	public static void playSample(String name, float pitch, float gain, boolean loop) {
		ResManager.get(Audio.class, name).playAsSoundEffect(pitch, gain, loop);
	}
	
	public static void playMusic(String name, float pitch, float gain, boolean loop) {
		ResManager.get(Audio.class, name).playAsMusic(pitch, gain, loop);
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
