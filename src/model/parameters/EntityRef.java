package model.parameters;

import java.util.HashMap;
import java.util.Map;

public class EntityRef {
	public enum EntityType {
		BreakableBlock, Goal, PlatformPlayer, SpringLike, SolidBlock, Bonus, Hazard, SlowMovementRegion
	}

	// TODO: Fix bug in grid transparency

	public static final Map<String, EntityType> textureEntityTypeMap = new HashMap<String, EntityType>();
	static {
		// just an alias
		Map<String, EntityType> tetm = textureEntityTypeMap;

		tetm.put("black", EntityType.SolidBlock);
		tetm.put("white", EntityType.BreakableBlock);
		tetm.put("flag", EntityType.Goal);
		tetm.put("gem", EntityType.Bonus);
		tetm.put("lava", EntityType.Hazard);
		tetm.put("player", EntityType.PlatformPlayer);
		tetm.put("spikes", EntityType.Hazard);
		tetm.put("spring", EntityType.SpringLike);
		// textureEntityTypeMap.put("water", EntityType.SlowMovementRegion);

		tetm.put("test_boxAlt", EntityType.BreakableBlock);
		tetm.put("test_brickWall", EntityType.BreakableBlock);
		tetm.put("test_cake", EntityType.BreakableBlock);
		tetm.put("test_cakeCenter", EntityType.BreakableBlock);
		tetm.put("test_cakeCenter_rounded", EntityType.BreakableBlock);
		tetm.put("test_cakeMid", EntityType.BreakableBlock);
		tetm.put("test_castle", EntityType.BreakableBlock);
		tetm.put("test_castleCenter", EntityType.BreakableBlock);
		tetm.put("test_castleCenter_rounded", EntityType.BreakableBlock);
		tetm.put("test_chimney", EntityType.BreakableBlock);
		tetm.put("test_choco", EntityType.BreakableBlock);
		tetm.put("test_chocoCenter", EntityType.BreakableBlock);
		tetm.put("test_chocoCenter_rounded", EntityType.BreakableBlock);
		tetm.put("test_chocoMid", EntityType.BreakableBlock);
		tetm.put("test_dirt", EntityType.BreakableBlock);
		tetm.put("test_dirtCenter", EntityType.BreakableBlock);
		tetm.put("test_dirtCenter_rounded", EntityType.BreakableBlock);
		tetm.put("test_dirtMid", EntityType.BreakableBlock);
		tetm.put("test_grass", EntityType.BreakableBlock);
		tetm.put("test_grassCenter", EntityType.BreakableBlock);
		tetm.put("test_grassCenter_rounded", EntityType.BreakableBlock);
		tetm.put("test_grassMid", EntityType.BreakableBlock);
		tetm.put("test_green", EntityType.BreakableBlock);
		tetm.put("test_hillCaneChoco", EntityType.BreakableBlock);
		tetm.put("test_hillCaneGreen", EntityType.BreakableBlock);
		tetm.put("test_hillCanePink", EntityType.BreakableBlock);
		tetm.put("test_hillCaneRed", EntityType.BreakableBlock);
		tetm.put("test_houseBeigeAlt", EntityType.BreakableBlock);
		tetm.put("test_houseDarkAlt", EntityType.BreakableBlock);
		tetm.put("test_houseGrayAlt", EntityType.BreakableBlock);
		tetm.put("test_iceBlock", EntityType.BreakableBlock);
		tetm.put("test_iceWaterDeep", EntityType.BreakableBlock);
		tetm.put("test_igloo", EntityType.BreakableBlock);
		tetm.put("test_iglooAlt", EntityType.BreakableBlock);
		tetm.put("test_lollipopGreen", EntityType.BreakableBlock);
		tetm.put("test_lollipopRed", EntityType.BreakableBlock);
		tetm.put("test_lollipopWhiteGreen", EntityType.BreakableBlock);
		tetm.put("test_lollipopWhiteRed", EntityType.BreakableBlock);
		tetm.put("test_metal", EntityType.BreakableBlock);
		tetm.put("test_metalMid", EntityType.BreakableBlock);
		tetm.put("test_metalRounded", EntityType.BreakableBlock);
		tetm.put("test_new.txt", EntityType.BreakableBlock);
		tetm.put("test_rockMoss", EntityType.BreakableBlock);
		tetm.put("test_roofGreyMid", EntityType.BreakableBlock);
		tetm.put("test_roofRedMid", EntityType.BreakableBlock);
		tetm.put("test_roofYellowMid", EntityType.BreakableBlock);
		tetm.put("test_sand", EntityType.BreakableBlock);
		tetm.put("test_sandMid", EntityType.BreakableBlock);
		tetm.put("test_signHangingCoin", EntityType.BreakableBlock);
		tetm.put("test_signHangingCup", EntityType.BreakableBlock);
		tetm.put("test_snowBallBig", EntityType.BreakableBlock);
		tetm.put("test_stone", EntityType.BreakableBlock);
		tetm.put("test_stoneCenter", EntityType.BreakableBlock);
		tetm.put("test_stoneCenter_rounded", EntityType.BreakableBlock);
		tetm.put("test_stoneMid", EntityType.BreakableBlock);
		tetm.put("test_tundra", EntityType.BreakableBlock);
		tetm.put("test_tundraCenter", EntityType.BreakableBlock);
		tetm.put("test_tundraCenter_rounded", EntityType.BreakableBlock);
		tetm.put("test_tundraMid", EntityType.BreakableBlock);
	}
}
