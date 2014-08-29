package unnamed_platformer.globals;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.res_mgt.ResManager;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;

public class EntityRef {

	public enum EntityParam {
		GRAPHIC, LOCATION, ORIENTATION, POWER, SUBCLASS, SIZE_STRATEGY
	}

	private static Map<String, Class<?>> textureNameToEntityClass = Maps
			.newHashMap();

	private static ListMultimap<Class<?>, String> entityClassToTextureName = ArrayListMultimap
			.create();

	public static final String PACKAGE_NAME = Ref.BASE_PACKAGE_NAME
			+ ".game.entities";

	public static List<String> getTexturesFromEntityClass(Class<?> entityClass) {
		return entityClassToTextureName.get(entityClass);
	}

	public static void addTextureNameToEntityClassMapping(String textureName,
			Class<?> entityClass) {

		if (!ResManager.contentExists(Texture.class, textureName)) {
			return;
		}
		EntityRef.textureNameToEntityClass.put(textureName, entityClass);
		EntityRef.entityClassToTextureName.put(entityClass, textureName);

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


}
