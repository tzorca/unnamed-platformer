package unnamed_platformer.game.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.content_management.ContentManager;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.AppGlobals;
import unnamed_platformer.globals.FileGlobals;
import unnamed_platformer.globals.GameGlobals.Flag;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;

public final class EntityLookup
{

	private static Map<String, Class<?>> textureNameToEntityClass = Maps
			.newHashMap();

	private static ListMultimap<Class<?>, String> entityClassToTextureName = ArrayListMultimap
			.create();

	public static final String PACKAGE_NAME = AppGlobals.PACKAGE_NAME
			+ ".game.entities";

	public static List<String> getTexturesFromEntityClass(Class<?> entityClass) {
		return entityClassToTextureName.get(entityClass);
	}

	public static void addTextureNameToEntityClassMapping(String textureName,
			Class<?> entityClass) {

		if (!ContentManager.contentExists(Texture.class,
				FileGlobals.IMG_OBJ_DIR, textureName)) {
			return;
		}
		EntityLookup.textureNameToEntityClass.put(textureName, entityClass);
		EntityLookup.entityClassToTextureName.put(entityClass, textureName);

	}

	public static Class<?> getEntityClassFromTextureName(String textureName) {
		return textureNameToEntityClass.get(textureName);
	}

	public static Set<String> getTextureNamesFromMap() {
		return textureNameToEntityClass.keySet();
	}

	public static boolean entityClassHasMapping(Class<?> entityClass) {
		return entityClassToTextureName.containsKey(entityClass);
	}

	public static boolean textureMapped(String texName) {
		return textureNameToEntityClass.containsKey(texName);
	}

	/**
	 * Get a list of entities with a given flag turned on.
	 * 
	 * @param entities
	 * @param flag
	 * @return
	 */
	public static List<Entity> select(Collection<Entity> entities, Flag flag) {
		List<Entity> selectedEntities = new ArrayList<Entity>();
		for (Entity entity : entities) {
			if (entity.isFlagSet(flag)) {
				selectedEntities.add(entity);
			}
		}
		return selectedEntities;
	}

}
