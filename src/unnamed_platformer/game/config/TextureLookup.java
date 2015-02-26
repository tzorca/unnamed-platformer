package unnamed_platformer.game.config;

import java.util.Map;

import com.google.common.collect.Maps;

public final class TextureLookup {

	private static Map<String, TextureSetup> textureSetups = Maps.newHashMap();

	public static void addSetup(String name, TextureSetup setup) {
		textureSetups.put(name,  setup);
	}

	public static TextureSetup getSetup(String textureName) {
		return textureSetups.get(textureName);
	}
}

