package unnamed_platformer.game.parameters;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import org.newdawn.slick.geom.Rectangle;

import unnamed_platformer.game.structures.FlColor;

public class Ref implements Serializable {
	private static final long serialVersionUID = 7052842507336198611L;

	public enum Flag {
		solid, hurtsOthers, obeysGravity, outOfPlay, dissolvesOnContact, tangible, breakableBlock, levelGoal, player, invisible, editLogic
	}

	public enum SizeMethod {
		ABSOLUTE, TEXTURE, TEXTURE_SCALE
	}

	
	public enum BlueprintField {
		levelBG, levelRect, levelEntities, levels, previewImage
	}
	
	public static <T> void multiadd(List<T> list, T[] elements) {
		for (T element : elements) {
			list.add(element);
		}
	}

	public static final FlColor DEFAULT_COLOR = new FlColor(1, 1, 1, 1);

	public static final Rectangle DEFAULT_LEVEL_RECTANGLE = new Rectangle(0, 0,
			2000, 4000);
	public static final int DEFAULT_LEVEL_GRIDSIZE = 32;

	public static final String APP_PATH = new File("").getAbsolutePath();

	public static final String RESOURCE_DIR = APP_PATH + File.separator + "res"
			+ File.separator;

	public static final String SCREENSHOT_DIR = APP_PATH + File.separator
			+ "scr" + File.separator;

	public static final int FPS = 60;

	public static final long MILLISECS_IN_IDEAL_TIC = (long) (1000.0 / (double) FPS);

	public static final int MASK_BLUR_ITERATIONS = 2;
	public static final float MASK_SIZE_PERCENT = .95f;

	public static final int MAX_SLOPE = 8;

	public static final String BASE_PACKAGE_NAME = "unnamed_platformer";

	public static final String APP_TITLE = "TODO: Title";

}
