package unnamed_platformer.app;

import java.awt.image.BufferedImage;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.content_management.CollisionDataLoader;
import unnamed_platformer.content_management.ContentManager;
import unnamed_platformer.content_management.ImageLoader;
import unnamed_platformer.content_management.SoundLoader;
import unnamed_platformer.content_management.TextureLoader;
import unnamed_platformer.game.config.GameConfig_Loader;
import unnamed_platformer.game.editor.EntityCreator;
import unnamed_platformer.game.editor.EntitySetup;
import unnamed_platformer.game.physics.CollisionData;
import unnamed_platformer.game.serialization.EntitySetupDeserializer;
import unnamed_platformer.game.serialization.LevelSerializer;
import unnamed_platformer.game.zones.Level;
import unnamed_platformer.game.zones.World;
import unnamed_platformer.globals.AppGlobals;
import unnamed_platformer.globals.FileGlobals;
import unnamed_platformer.input.GameKey;
import unnamed_platformer.input.InputManager;
import unnamed_platformer.view.GUIManager;
import unnamed_platformer.view.GUIManager.ScreenType;
import unnamed_platformer.view.ViewManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rits.cloning.Cloner;

public final class Main
{
	private static long accumulator = 0;

	private static boolean hotkeysAllowed = true;

	private static void gameLoop() {
		while (!Display.isCloseRequested()) {
			long millisecDelta = TimeManager.tick();

			if (millisecDelta == 0) {
				continue;
			}

			InputManager.update();
			if (hotkeysAllowed) {
				handleHotkeys();
			}

			accumulator += millisecDelta;

			while (accumulator >= AppGlobals.IDEAL_TIC_MILLISECONDS) {
				World.update();
				accumulator -= AppGlobals.IDEAL_TIC_MILLISECONDS;
			}

			ViewManager.update();
		}
	}

	private static void handleHotkeys() {
		if (InputManager.keyPressOccurred(GameKey.RESTART, 1)) {
			GUIManager.changeScreen(ScreenType.Title);
		}

		else if (InputManager.keyPressOccurred(GameKey.SAVE_SCREENSHOT, 1)) {
			ViewManager.saveScreenshot();
		}

		else if (InputManager.keyPressOccurred(GameKey.EXIT, 1)) {
			doHalt();
		}

		// else if (InputManager.keyPressOccurred(GameKey.TESTING, 1)) {
		// World.saveGson("test-json");
		// }

		// else if (InputManager.keyPressOccurred(GameKey.back, 1)) {
		// GUIManager.back();
		// }
		// else if (InputManager.gameKeyPressed(GameKey.saveTempGame, 1)) {
		// World.save("Temp");
		// }

	}

	public static void main(String[] args) {
		init();
		gameLoop();
	}

	private static void init() {
		// used to load native libraries without relying on project
		// configuration
		System.setProperty("org.lwjgl.librarypath", FileGlobals.NATIVE_LIB_DIR);
		System.setProperty("net.java.games.input.librarypath",
				FileGlobals.NATIVE_LIB_DIR);

		setupCloner();
		setupGson();

		ClassLookup.init();
		setupContentLoaders();
		Settings.init();
		TimeManager.init();
		EntityCreator.init();
		InputManager.init();
		GUIManager.init();
		GameConfig_Loader.init();
		ViewManager.init();
		GameConfig_Loader.extractConfig();
		GUIManager.changeScreen(ScreenType.Title);
	}

	private static void setupContentLoaders() {
		ContentManager.registerContentLoader(BufferedImage.class, new ImageLoader());
		ContentManager.registerContentLoader(Texture.class, new TextureLoader());
		ContentManager.registerContentLoader(Audio.class, new SoundLoader());
		ContentManager.registerContentLoader(CollisionData.class, new CollisionDataLoader());
		

		new Thread(AudioManager.preloader).start();
	}

	private static Cloner cloner = new Cloner();
	private static Gson gson;

	private static void setupCloner() {
		cloner.dontClone(Texture.class);
	}

	private static void setupGson() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Level.class, new LevelSerializer());
		builder.registerTypeAdapter(EntitySetup.class,
				new EntitySetupDeserializer());
		gson = builder.create();
	}

	public static Gson getGson() {
		return gson;
	}

	public static <T> T deepClone(T o) {
		return cloner.deepClone(o);
	}

	public static void doExit() {
		deinit();
		System.exit(0);
	}

	public static void doHalt() {
		deinit();
		Runtime.getRuntime().halt(0);
	}

	public static void deinit() {
		InputManager.finish();
		AudioManager.finish();
	}

	public static void setHotKeysAllowed(boolean value) {
		hotkeysAllowed = value;
	}

}
