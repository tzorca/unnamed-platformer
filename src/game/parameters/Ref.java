package game.parameters;

import game.structures.FlColor;

import java.io.File;
import java.io.Serializable;

import org.newdawn.slick.geom.Rectangle;

public class Ref implements Serializable {
	private static final long serialVersionUID = 7052842507336198611L;

	public enum Flag {
		solid, hurtsOthers, obeysGravity, outOfPlay, dissolvesOnContact, tangible, breakableBlock, levelGoal, player, invisible, editLogic
	}

	public enum SizeMethod {
		ABSOLUTE, TEXTURE, TEXTURE_SCALE
	}

	public enum BlueprintComponent {
		levelBG, levelRect, levelEntities, levelPlayerEntity, levels, gameName
	}

	public static final FlColor DEFAULT_COLOR = new FlColor(1, 1, 1, 1);

	public static final Rectangle DEFAULT_LEVEL_RECTANGLE = new Rectangle(0, 0,
			2000, 4000);
	public static final int DEFAULT_LEVEL_GRIDSIZE = 32;


	public static String appPath = new File("").getAbsolutePath();

	public static String resourceDir = appPath + File.separator + "res" + File.separator;

	public static final int FPS = 60;

	public static final long MILLISECS_IN_IDEAL_TIC = (long) (1000.0 / (double) FPS);

	public static final int MASK_BLUR_ITERATIONS = 2;
	public static final float MASK_SIZE_PERCENT = .95f;

	public static final int MAX_SLOPE = 8;

}
