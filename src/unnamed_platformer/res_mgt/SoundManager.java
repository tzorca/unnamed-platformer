package unnamed_platformer.res_mgt;

import java.util.Collection;
import java.util.List;

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

	public static void update() {
		SoundStore.get().poll(0);
	}

	public static void finish() {
		AL.destroy();
	}

	public static void preload() {
		Collection<String> audioNames = ResManager.list(Audio.class, true);
		
		for (String audioName : audioNames) {
			ResManager.get(Audio.class, audioName);
		}
	}
}
