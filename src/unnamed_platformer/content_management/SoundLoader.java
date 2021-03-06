package unnamed_platformer.content_management;

import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

public class SoundLoader extends ContentLoader {

	public SoundLoader() {
		super(".ogg");
	}

	@Override
	protected Object load(String directory, String name, String ext)
			throws Exception {
		return AudioLoader.getAudio("OGG",
				ResourceLoader.getResourceAsStream(getFilename(directory, name, ext)));
	}
}
