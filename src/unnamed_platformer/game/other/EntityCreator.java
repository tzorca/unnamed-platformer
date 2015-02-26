package unnamed_platformer.game.other;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.ClassLookup;
import unnamed_platformer.app.MathHelper;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameGlobals.EntityParam;
import unnamed_platformer.view.Graphic;

import com.google.common.collect.Maps;

// TODO: Add customizable entity orientation
public final class EntityCreator {

	// Setup texture entity subclass mappings
	// and load textures and collisiondata
	public static void init() {
		// setupTextureMappings();
		cacheEntityConstructors();
	}

	private static void cacheEntityConstructors() {
		Collection<Class<?>> classes = ClassLookup
				.getClassesInPackage(EntityLookup.PACKAGE_NAME);

		for (Class<?> clazz : classes) {
			try {
				entityConstructorCache.put(clazz,
						clazz.getConstructor(EntitySetup.class));
			} catch (Exception e) {
				System.out.println("Warning: Entity class '" + clazz.getName()
						+ "' has not implemented its constructor.");
			}
		}
	}

	public static Entity create(Class<?> entityClass, Vector2f vector2f,
			float sizeInput, boolean relativeSize) {
		if (entityClass == null) {
			System.out
					.println("Can't create an entity with an empty entityClass.");
			return null;
		}
		String textureName = (String) MathHelper.randInList(EntityLookup
				.getTexturesFromEntityClass(entityClass));

		return create(textureName, entityClass, vector2f, sizeInput,
				relativeSize);
	}

	public static Entity create(String textureName, Vector2f location,
			float sizeInput, boolean relativeSize) {
		Class<?> entityClass = EntityLookup
				.getEntityClassFromTextureName(textureName);
		if (entityClass == null) {

			System.out.println("Texture '" + textureName
					+ "' is missing a entity class mapping.");
			return null;
		}
		return create(textureName, entityClass, location, sizeInput,
				relativeSize);
	}

	public static Entity create(String textureName, Class<?> entityClass,
			Vector2f location, float sizeInput, boolean relativeSize) {

		EntitySetup setup = new EntitySetup();

		setup.set(EntityParam.SIZE_STRATEGY, new SizeStrategy(
				relativeSize ? SizeStrategy.Strategy.textureScale
						: SizeStrategy.Strategy.absoluteWidth, sizeInput));
		setup.set(EntityParam.GRAPHIC, new Graphic(textureName));
		setup.set(EntityParam.LOCATION, location);
		setup.setEntityClassName(entityClass.getSimpleName());

		return buildFromSetup(setup);

	}

	public static Entity buildFromSetup(EntitySetup setup) {
		Entity newEntity = null;
		Class<?> entityClass = ClassLookup.getClass(EntityLookup.PACKAGE_NAME,
				setup.getEntityClassName());
		try {
			newEntity = (Entity) getConstructor(entityClass).newInstance(setup);
		} catch (Exception e) {
			System.out.println("Warning: Class '" + entityClass.toString()
					+ "' has an implementation error: " + e.toString());
			e.printStackTrace();
			return null;
		}

		if (newEntity == null) {
			System.out.println("Warning: '" + entityClass.toString()
					+ "' was created null.");
		}

		return newEntity;
	}

	public static Entity create(String textureName, Vector2f location) {
		return create(textureName, location, 1, true);
	}

	public static boolean hasMapping(String texName) {
		return EntityLookup.textureMapped(texName);
	}

	public static Set<String> listTextureNames() {
		return EntityLookup.getTextureNamesFromMap();
	}

	public static Entity create(String textureName, Vector2f location, int width) {
		return create(textureName, location, width, false);
	}

	public static String chooseTextureFromType(Class<?> entityClass) {
		if (!EntityLookup.entityClassHasMapping(entityClass)) {
			return null;
		}

		return (String) MathHelper.randInList(EntityLookup
				.getTexturesFromEntityClass(entityClass));
	}

	public static LinkedList<Entity> buildFromSetupCollection(
			LinkedList<EntitySetup> setups) {
		LinkedList<Entity> entities = new LinkedList<Entity>();

		for (EntitySetup setup : setups) {
			Entity newEntity = buildFromSetup(setup);
			if (newEntity != null) {
				entities.add(newEntity);
			}
		}
		return entities;
	}

	public static LinkedList<EntitySetup> getSetupCollection(
			LinkedList<Entity> entities) {
		LinkedList<EntitySetup> entitySetups = new LinkedList<EntitySetup>();

		for (Entity entity : entities) {
			entitySetups.add(entity.getOriginalSetup());
		}
		return entitySetups;
	}

	private static HashMap<Class<?>, Constructor<?>> entityConstructorCache = Maps
			.newHashMap();

	private static Constructor<?> getConstructor(Class<?> clazz) {
		return entityConstructorCache.get(clazz);
	}
}
