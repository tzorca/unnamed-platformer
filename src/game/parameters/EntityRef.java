package game.parameters;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.reflect.ClassPath;

public class EntityRef {

	private static Map<String, Class> textureName_EntityClass_Map = Maps
			.newHashMap();

	private static ListMultimap<Class, String> entityClass_TextureName_Map = ArrayListMultimap
			.create();

	private static Map<String, Class> className_EntityClass_Map = Maps
			.newHashMap();

	static {
		ClassPath classpath;
		try {
			classpath = ClassPath.from(ClassLoader.getSystemClassLoader());
			for (ClassPath.ClassInfo classInfo : classpath
					.getTopLevelClasses("game.entities")) {
				String name = classInfo.getSimpleName();
				Class entityClass = classInfo.load();

				className_EntityClass_Map.put(name, entityClass);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Collection<Class> getEntitySubclasses() {
		return className_EntityClass_Map.values();
	}

	public static Collection<String> getEntitySubclassNames() {
		return className_EntityClass_Map.keySet();
	}

	public static Class getClassFromClassName(String className) {
		return className_EntityClass_Map.get(className);
	}

	public static boolean classNameExists(String className) {
		return className_EntityClass_Map.containsKey(className);
	}

	public static List<String> getTexturesFromEntityClass(Class entityClass) {
		return entityClass_TextureName_Map.get(entityClass);
	}

	public static void addTextureNameToEntityClassMapping(String textureName,
			Class entityClass) {

		EntityRef.textureName_EntityClass_Map.put(textureName, entityClass);
		EntityRef.entityClass_TextureName_Map.put(entityClass, textureName);

	}

	public static Class getEntityClassFromTextureName(String textureName) {
		return textureName_EntityClass_Map.get(textureName);
	}

	public static Set<String> getTextureNamesFromMap() {
		return textureName_EntityClass_Map.keySet();
	}

	public static boolean entityClassHasMapping(Class entityClass) {
		return entityClass_TextureName_Map.containsKey(entityClass);
	}

	public static boolean textureMapped(String texName) {
		return textureName_EntityClass_Map.containsKey(texName);
	}

}
