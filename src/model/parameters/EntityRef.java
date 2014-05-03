package model.parameters;

import java.util.HashMap;
import java.util.Map;

public class EntityRef {
	public enum EntityType {
		BreakableBlock, Goal, PlatformPlayer, SpringLike, SolidBlock, Bonus, Hazard, SlowMovementRegion
	}

	public static final Map<String, EntityType> textureEntityTypeMap = new HashMap<String, EntityType>();
	static {
		textureEntityTypeMap.put("black", EntityType.SolidBlock);
		textureEntityTypeMap.put("white", EntityType.BreakableBlock);
		textureEntityTypeMap.put("flag", EntityType.Goal);
		textureEntityTypeMap.put("gem", EntityType.Bonus);
		textureEntityTypeMap.put("lava", EntityType.Hazard);
		textureEntityTypeMap.put("player", EntityType.PlatformPlayer);
		textureEntityTypeMap.put("spikes", EntityType.Hazard);
		textureEntityTypeMap.put("spring", EntityType.SpringLike);
		// textureEntityTypeMap.put("water", EntityType.SlowMovementRegion);
	}
}
