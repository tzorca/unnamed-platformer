package unnamed_platformer.game;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.MathHelper;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.EntityRef;
import unnamed_platformer.globals.EntityRef.EntityParam;
import unnamed_platformer.res_mgt.ClassLookup;
import unnamed_platformer.structures.Graphic;
import unnamed_platformer.structures.SizeStrategy;

import com.google.common.collect.Maps;

// TODO: Customizable entity orientation
public class EntityCreator {

	// Setup texture entity subclass mappings
	// and load textures and binarypixelgrid
	public static void init() {
//		setupTextureMappings();
		cacheEntityConstructors();
	}

	private static void cacheEntityConstructors() {
		Collection<Class<?>> classes = ClassLookup.getClassesInPackage(EntityRef.PACKAGE_NAME);

		for (Class<?> clazz : classes) {
			try {
				entityConstructorCache.put(clazz, clazz.getConstructor(EntitySetup.class));
			} catch (Exception e) {
				System.out
						.println("Warning: Entity class '" + clazz.getName() + "' has not implemented its constructor.");
			}
		}
	}

//	private static void setupTextureMappings() {
//		Collection<String> textureNames = ResManager.list(Texture.class, true);
//
//		for (String internalTextureName : textureNames) {
//
//			// get possibleclassname
//			String possibleClassName = ResManager.getClassNameFromContentName(internalTextureName);
//
//			if (possibleClassName == null) {
//				continue;
//			}
//
//			// check if possibleClassName was a valid entity subclass
//			if (!ClassLookup.classExists(EntityRef.PACKAGE_NAME, possibleClassName)) {
//				continue;
//			}
//
//			Class<?> entityClass = ClassLookup.getClass(EntityRef.PACKAGE_NAME, possibleClassName);
//
//			// If the possibleClassName was correct, we can
//			// add the entity as a creatable entity
//			EntityRef.addTextureNameToEntityClassMapping(internalTextureName, entityClass);
//		
////			ContentManager.get(ContentType.texture,internalTextureName);
//		}
//	}

	public static Entity create(Class<?> entityClass, Vector2f vector2f, float sizeInput, boolean relativeSize) {
		if (entityClass == null) {
			System.out.println("Can't create an entity with an empty entityClass.");
			return null;
		}
		String textureName = (String) MathHelper.randInList(EntityRef.getTexturesFromEntityClass(entityClass));

		return create(textureName, entityClass, vector2f, sizeInput, relativeSize);
	}

	public static Entity create(String textureName, Vector2f location, float sizeInput, boolean relativeSize) {
		Class<?> entityClass = EntityRef.getEntityClassFromTextureName(textureName);
		if (entityClass == null) {

			System.out.println("Texture '" + textureName + "' is missing a entity class mapping.");
			return null;
		}
		return create(textureName, entityClass, location, sizeInput, relativeSize);
	}

	public static Entity create(String textureName, Class<?> entityClass, Vector2f location, float sizeInput,
			boolean relativeSize) {

		EntitySetup setup = new EntitySetup();

		setup.set(EntityParam.sizeStrategy, new SizeStrategy(relativeSize ? SizeStrategy.Strategy.textureScale
				: SizeStrategy.Strategy.absoluteWidth, sizeInput));
		setup.set(EntityParam.graphic, new Graphic(textureName));
		setup.set(EntityParam.location, location);
		setup.setEntityClassName(entityClass.getSimpleName());

		return buildFromSetup(setup);

	}

	private static Entity buildFromSetup(EntitySetup setup) {
		Entity newEntity = null;
		Class<?> entityClass = ClassLookup.getClass(EntityRef.PACKAGE_NAME, setup.getEntityClassName());
		try {
			newEntity = (Entity) getConstructor(entityClass).newInstance(setup);
		} catch (Exception e) {
			System.out.println("Warning: Class '" + entityClass.toString() + "' has an implementation error: " + e.toString());
			return null;
		}

		if (newEntity == null) {
			System.out.println("Warning: '" + entityClass.toString() + "' was created null.");
		}

		return newEntity;
	}

	public static Entity create(String textureName, Vector2f location) {
		return create(textureName, location, 1, true);
	}

	public static boolean hasMapping(String texName) {
		return EntityRef.textureMapped(texName);
	}

	public static Set<String> listTextureNames() {
		return EntityRef.getTextureNamesFromMap();
	}

	public static Entity create(String textureName, Vector2f location, int width) {
		return create(textureName, location, width, false);
	}

	public static String chooseTextureFromType(Class<?> entityClass) {
		if (!EntityRef.entityClassHasMapping(entityClass)) {
			return null;
		}

		return (String) MathHelper.randInList(EntityRef.getTexturesFromEntityClass(entityClass));
	}

	public static LinkedList<Entity> buildFromSetupCollection(LinkedList<EntitySetup> setups) {
		LinkedList<Entity> entities = new LinkedList<Entity>();

		for (EntitySetup setup : setups) {
			Entity newEntity = buildFromSetup(setup);
			if (newEntity != null) {
				entities.add(newEntity);
			}
		}
		return entities;
	}

	public static LinkedList<EntitySetup> getSetupCollection(LinkedList<Entity> entities) {
		LinkedList<EntitySetup> entitySetups = new LinkedList<EntitySetup>();

		for (Entity entity : entities) {
			entitySetups.add(entity.getOriginalSetup());
		}
		return entitySetups;
	}

	private static HashMap<Class<?>, Constructor<?>> entityConstructorCache = Maps.newHashMap();

	private static Constructor<?> getConstructor(Class<?> clazz) {
		return entityConstructorCache.get(clazz);
	}
}
