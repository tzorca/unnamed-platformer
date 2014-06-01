package unnamed_platformer.game.parameters;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import unnamed_platformer.app.ClassLookup;
import unnamed_platformer.game.EntitySetup;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;

public class EntityRef {

	public enum EntityParam {
		graphic, location, orientation, width, power, subclass
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
