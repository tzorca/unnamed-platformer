package unnamed_platformer.globals;

import java.io.File;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;

public final class Ref {

	public enum SizeMethod {
		ABSOLUTE, TEXTURE, TEXTURE_SCALE
	}

	public enum BlueprintField {
		LEVEL_BG, LEVEL_RECT, LEVEL_ENTITIES, LEVEL_DATA, PREVIEW_IMAGE
	}

	public static final Color DEFAULT_COLOR = new Color(1, 1, 1, 1);
	public static final Color COLOR_75_PERCENT_TRANS = new Color(1, 1, 1, 0.75f);

	public static final Rectangle DEFAULT_LEVEL_RECTANGLE = new Rectangle(0, 0,
			16000, 4000);
	public static final int DEFAULT_LEVEL_GRIDSIZE = 32;

	public static final String APP_PATH = new File("").getAbsolutePath();

	public static final String NATIVE_LIB_DIR = APP_PATH + File.separator
			+ "native-lib" + File.separator;

	public static final String RESOURCE_DIR = APP_PATH + File.separator + "res"
			+ File.separator;

	public static final String SCREENSHOT_DIR = APP_PATH + File.separator
			+ "scr" + File.separator;

	public static final float INPUT_REPEAT_TIME = 0.1f;
	public static final float INPUT_DELAY_TIME = 0.25f;

	public static final int FPS = 60;

	public static final long MILLISECS_IN_IDEAL_TIC = (long) (1000.0 / (double) FPS);

	public static final int MASK_BLUR_ITERATIONS = 2;
	public static final float MASK_SIZE_PERCENT = .95f;

	public static final int MAX_SLOPE = 8;

	public static final String BASE_PACKAGE_NAME = "unnamed_platformer";

	public static final String APP_TITLE = "Unnamed Platformer";

}
