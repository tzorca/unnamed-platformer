package unnamed_platformer.globals;

import java.io.File;

public class FileGlobals
{
	public static final String
	/* */APP_PATH = new File("").getAbsolutePath() + File.separator,

	/* */NATIVE_LIB_DIR = APP_PATH + "native-lib" + File.separator,

	/* */RESOURCE_DIR = APP_PATH + "res" + File.separator,

	/* */SCREENSHOT_DIR = APP_PATH + "scr" + File.separator,

	/* */GAME_DIR = RESOURCE_DIR + "game" + File.separator,
	/* */IMG_BG_DIR = RESOURCE_DIR + "img-bg" + File.separator,
	/* */IMG_GUI_DIR = RESOURCE_DIR + "img-gui" + File.separator,
	/* */IMG_OBJ_DIR = RESOURCE_DIR + "img-obj" + File.separator,
	/* */IMG_PREVIEW_DIR = RESOURCE_DIR + "img-preview" + File.separator,
	/* */SND_DIR = RESOURCE_DIR + "snd" + File.separator,

	/* */GAME_EXT = ".json";

	public static final File
	/* */SETTINGS_FILE = new File(APP_PATH, "settings.ini"),
	/* */GAME_CONFIG_FILE = new File(RESOURCE_DIR, "game-config.json");

}
