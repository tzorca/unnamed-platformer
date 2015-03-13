package unnamed_platformer.app;

import java.util.Collection;

import org.lwjgl.openal.AL;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.SoundStore;

import unnamed_platformer.content_management.ContentManager;
import unnamed_platformer.globals.FileGlobals;

public class AudioManager
{
	public static void playSample(String name) {
		ContentManager.get(Audio.class, FileGlobals.SND_DIR, name)
				.playAsSoundEffect(1.0f, 1.0f, false);
	}

	public static void playSampleIfNotPlaying(String name) {
		Audio sample = ContentManager.get(Audio.class, FileGlobals.SND_DIR,
				name);
		if (!sample.isPlaying()) {
			sample.playAsSoundEffect(1.0f, 1.0f, false);
		}
	}

	public static void playMusic(String name, float pitch, float gain,
			boolean loop) {
		ContentManager.get(Audio.class, FileGlobals.SND_DIR, name).playAsMusic(
				pitch, gain, loop);
	}

	public static void update() {
		SoundStore.get().poll(0);
	}

	public static void finish() {
		AL.destroy();
	}

	public static Runnable preloader = new Runnable() {
		public void run() {
			Collection<String> audioNames = FileHelper.listFilenames(
					FileGlobals.SND_DIR, true);

			for (String audioName : audioNames) {
				ContentManager.get(Audio.class, FileGlobals.SND_DIR, audioName);
			}
		}
	};
}
