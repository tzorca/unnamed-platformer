package model.parameters;

import java.awt.Rectangle;
import java.io.Serializable;

import model.structures.FlColor;

public class Ref implements Serializable {
	private static final long serialVersionUID = 7052842507336198611L;

	public enum Flag {
		solid, hurtsYou, hurtsOthers, obeysGravity, outOfPlay, dissolvesOnContact, tangible, breakableBlock, levelGoal, player, invisible, editLogic
	}

	public enum SizeMethod {
		ABSOLUTE, TEXTURE, TEXTURE_SCALE
	}

	public enum BlueprintComponent {
		levelBackgroundTexture, levelRect, levelEntities, levelPlayerEntity, levels, gameName
	}

	public static final FlColor DEFAULT_BG_COLOR = new FlColor(176f / 255f,
			196f / 255f, 222 / 255f);

	public static final Rectangle DEFAULT_LEVEL_RECTANGLE = new Rectangle(0, 0,
			2000, 4000);
	public static final int DEFAULT_LEVEL_GRIDSIZE = 32;

	public static String baseDir = "res/";
}
