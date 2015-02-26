package unnamed_platformer.globals;

import java.io.File;

public class FileGlobals
{
	public static final String
	/* */APP_PATH = new File("").getAbsolutePath(),

	/* */NATIVE_LIB_DIR = APP_PATH + File.separator + "native-lib"
			+ File.separator,

	/* */RESOURCE_DIR = APP_PATH + File.separator + "res" + File.separator,

	/* */SCREENSHOT_DIR = APP_PATH + File.separator + "scr" + File.separator;

	public static final File
	/* */SETTINGS_FILE = new File(APP_PATH, "settings.ini"),
	/* */GAME_CONFIG_FILE = new File(RESOURCE_DIR, "game-config.json");

}
