package unnamed_platformer.game.config;

import java.io.IOException;

import org.apache.commons.io.FileUtils;

import unnamed_platformer.app.ClassLookup;
import unnamed_platformer.app.Main;
import unnamed_platformer.game.editor.EntityLookup;
import unnamed_platformer.globals.FileGlobals;
import unnamed_platformer.view.ViewManager;

public class GameConfig_Loader
{
	private static GameConfig gameDB;

	public static void init() {
		readConfig();
		loadTextureMappings();
	}

	public static void readConfig() {
		String data = null;
		try {
			data = FileUtils.readFileToString(FileGlobals.GAME_CONFIG_FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		gameDB = Main.getGson().fromJson(data, GameConfig.class);
	}

	public static void loadTextureMappings() {
		for (final String textureName : gameDB.getTextureMappings().keySet()) {
			TextureLinks links = gameDB.getTextureMappings().get(textureName);
			final String entityClassName = links.entityName;

			if (entityClassName.equals("none")) {
				continue;
			}
			
			final Class<?> entityClass = ClassLookup.getClass(
					EntityLookup.PACKAGE_NAME, entityClassName);

			ViewManager.doWhenActive(new Runnable() {
				public void run() {
					EntityLookup.addTextureNameToEntityClassMapping(textureName,
							entityClass);
				}
			});
			String collisionShape = links.collisionShape;
			TextureLookup.addSetup(textureName, new TextureSetup(collisionShape));

		}
	}

}
