package model;

import java.awt.Rectangle;
import java.io.Serializable;

public class Ref implements Serializable {
	private static final long serialVersionUID = 7052842507336198611L;

	public enum Orientation {
		UP, DOWN, LEFT, RIGHT, UPRIGHT, UPLEFT, DOWNRIGHT, DOWNLEFT
	}

	public enum Axis {
		HORIZONTAL, VERTICAL
	}

	public enum Flag {
		solid, hurtsYou, hurtsOthers, obeysGravity, outOfPlay, dissolvesOnContact, tangible, breakableBlock, levelGoal, player, invisible, editLogic
	}

	public enum SizeMethod {
		ABSOLUTE, TEXTURE, TEXTURE_SCALE
	}

	public enum BlueprintComponent {
		levelBackgroundTexture, levelRect, levelEntities, levelPlayerEntity, levels, gameName
	}

	public static final  Rectangle DEFAULT_LEVEL_RECTANGLE = new Rectangle(0, 0,
			2000, 4000);
	public static final int DEFAULT_LEVEL_GRIDSIZE = 32;

	public static final float DEFAULT_PLR_SPEED = 3;
	public static final double DEFAULT_PLR_JUMP_STRENGTH = 5;
	public static final double DEFAULT_PLR_JUMP_TIME = 0.5;
	public static final float DEFAULT_SPRING_STRENGTH = 1;

	public enum QuickType {
		EditCamera, Player
	}

	public static String baseDir = "res/";
}
