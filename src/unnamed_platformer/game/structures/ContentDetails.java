package unnamed_platformer.game.structures;

import unnamed_platformer.game.parameters.Ref;

public class ContentDetails {
	public String dir = "", ext;
	@SuppressWarnings("rawtypes")
	public Class classType;
	public boolean cacheable = false;

	public ContentDetails(String dir, String ext,
			@SuppressWarnings("rawtypes") Class classType, boolean cacheable) {
		this.dir = dir;
		this.ext = ext;
		this.classType = classType;
		this.cacheable = cacheable;
	}

	public String getFilename(String name) {
		return Ref.RESOURCE_DIR + dir + name + ext;
	}

	public String getDir() {
		return Ref.RESOURCE_DIR + dir;
	}

}