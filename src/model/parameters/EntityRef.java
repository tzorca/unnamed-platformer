package model.parameters;

import java.util.HashMap;
import java.util.Map;

public class EntityRef {
	public enum EntityType {
		BreakableBlock, Goal, PlatformPlayer, SpringLike, SolidBlock, Bonus, Hazard, SlowMovementRegion
	}

	public static Map<String, EntityType> textureEntityTypeMap = new HashMap<String, EntityType>();
	static {
		// just an alias
//		Map<String, EntityType> tetm = textureEntityTypeMap;
//
//		tetm.put("black", EntityType.SolidBlock);
//		tetm.put("white", EntityType.BreakableBlock);
//		tetm.put("flag", EntityType.Goal);
//		tetm.put("gem", EntityType.Bonus);
//		tetm.put("lava", EntityType.Hazard);
//		tetm.put("player", EntityType.PlatformPlayer);
//		tetm.put("spikes", EntityType.Hazard);
//		tetm.put("spring", EntityType.SpringLike);
//		// textureEntityTypeMap.put("water", EntityType.SlowMovementRegion);
	}
}
