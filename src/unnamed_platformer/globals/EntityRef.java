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
		graphic, location, orientation, power, subclass, sizeStrategy
	}

	public enum PotentialEntityParams {
		levelWarp, locationWarp, solid
	}

	private static Map<String, Class<?>> textureName_EntityClass_Map = Maps
			.newHashMap();

	private static ListMultimap<Class<?>, String> entityClass_TextureName_Map = ArrayListMultimap
			.create();

	public static final String PACKAGE_NAME = Ref.BASE_PACKAGE_NAME
			+ ".game.entities";

	public static List<String> getTexturesFromEntityClass(Class<?> entityClass) {
		return entityClass_TextureName_Map.get(entityClass);
	}

	public static void addTextureNameToEntityClassMapping(String textureName,
			Class<?> entityClass) {

		if (!ResManager.contentExists(Texture.class, textureName)) {
			return;
		}
		EntityRef.textureName_EntityClass_Map.put(textureName, entityClass);
		EntityRef.entityClass_TextureName_Map.put(entityClass, textureName);

	}

	public static Class<?> getEntityClassFromTextureName(String textureName) {
		return textureName_EntityClass_Map.get(textureName);
	}

	public static Set<String> getTextureNamesFromMap() {
		return textureName_EntityClass_Map.keySet();
	}

	public static boolean entityClassHasMapping(Class<?> entityClass) {
		return entityClass_TextureName_Map.containsKey(entityClass);
	}

	public static boolean textureMapped(String texName) {
		return textureName_EntityClass_Map.containsKey(texName);
	}


}
