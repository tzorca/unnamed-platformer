package unnamed_platformer.globals;

import java.util.Map;

import unnamed_platformer.structures.TextureSetup;

import com.google.common.collect.Maps;

public class TextureRef {

	private static Map<String, TextureSetup> textureSetups = Maps.newHashMap();

	public static void addSetup(String name, TextureSetup setup) {
		textureSetups.put(name,  setup);
	}

}
