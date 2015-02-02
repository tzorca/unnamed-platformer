package unnamed_platformer.game.config;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.app.Main;
import unnamed_platformer.game.other.TextureSetup;
import unnamed_platformer.globals.EntityRef;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.globals.TextureRef;
import unnamed_platformer.res_mgt.ClassLookup;
import unnamed_platformer.res_mgt.ResManager;
import unnamed_platformer.view.ViewManager;

public class GameConfig_Loader
{
	private static GameConfig gameDB;

	public static void init() {
		readConfig();
		addNewTextureNames();
		loadTextureMappings();
	}

	public static void readConfig() {
		String data = null;
		try {
			data = FileUtils.readFileToString(Ref.GAME_CONFIG_FILE);
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

			if (!entityClassName.equals("none")) {
				final Class<?> entityClass = ClassLookup.getClass(
						EntityRef.PACKAGE_NAME, entityClassName);
				
				ViewManager.doWhenActive(new Runnable() {
					public void run() {
						EntityRef.addTextureNameToEntityClassMapping(
								textureName, entityClass);
					}
				});
			}
			String collisionShape = links.collisionShape;
			TextureRef.addSetup(textureName, new TextureSetup(collisionShape));
		}
	}

	private static void addNewTextureNames() {
		Collection<String> textureNames = ResManager.list(Texture.class, true);

		Map<String, TextureLinks> gameDBTextureMappings = gameDB
				.getTextureMappings();

		for (String textureName : textureNames) {
			if (!gameDBTextureMappings.containsKey(textureName)) {
				gameDBTextureMappings.put(textureName, new TextureLinks("none",
						"none"));
			}
		}
	}

}
