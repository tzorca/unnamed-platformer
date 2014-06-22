package unnamed_platformer.res_mgt;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

public class UntestedAudioLoader extends ResLoader<Audio> {

	protected UntestedAudioLoader() {
		super("audio", ".ogg");
	}

	@Override
	protected Audio load(String name) throws Exception {
		return AudioLoader.getAudio("OGG",
				ResourceLoader.getResourceAsStream(getFilename(name)));
	}
}
