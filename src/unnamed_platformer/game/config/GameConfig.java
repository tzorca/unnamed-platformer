package unnamed_platformer.game.config;

import java.util.HashMap;
import java.util.Map;

public class GameConfig
{
	private Map<String, TextureLinks> textureMappings = new HashMap<String, TextureLinks>();

	public Map<String, TextureLinks> getTextureMappings() {
		return textureMappings;
	}

}
