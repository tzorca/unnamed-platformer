package unnamed_platformer.game.logic;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.opengl.Texture;

import com.google.common.collect.Maps;

import unnamed_platformer.app.App;
import unnamed_platformer.app.ClassLookup;
import unnamed_platformer.app.ContentManager;
import unnamed_platformer.game.EntitySetup;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.parameters.ContentRef.ContentType;
import unnamed_platformer.game.parameters.EntityRef;
import unnamed_platformer.game.parameters.EntityRef.EntityParam;
import unnamed_platformer.game.structures.Graphic;

// TODO: Customizable entity orientation
public class EntityCreator {

	// Setup texture entity subclass mappings
	// and load textures and binarypixelgrid
	public static void init() {
		setupTextureMappings();
		cacheEntityConstructors();
	}

	private static void cacheEntityConstructors() {
		Collection<Class<?>> classes = ClassLookup
				.getClassesInPackage(EntityRef.PACKAGE_NAME);

		for (Class<?> clazz : classes) {
			try {
				entityConstructorCache.put(clazz,
						clazz.getConstructor(EntitySetup.class));
			} catch (Exception e) {
				System.out.println("Warning: Entity class " + clazz.getName()
						+ " has not implemented its constructor.");
			}
		}
	}

	private static void setupTextureMappings() {
		Map<File, String> textureFiles = ContentManager
				.getFileNameMap(ContentType.texture);

		for (Entry<File, String> entry : textureFiles.entrySet()) {

			// TODO: More auto entity classification -> dev productivity++
			// TODO: Replace folder system (don't load textures on startup)

			// cache texture and binarypixelgrid
			ContentManager.customCache(ContentType.texture, entry.getValue(),
					entry.getKey());
			ContentManager.customCache(ContentType.binaryPixelGrid,
					entry.getValue(), entry.getKey());

			// get possibleclassname
			String possibleClassName = new File(entry.getKey().getParent())
					.getName();

			if (possibleClassName == null) {
				continue;
			}

			// check if possibleClassName was a valid entity subclass
			if (!ClassLookup.classExists(EntityRef.PACKAGE_NAME,
					possibleClassName)) {
				continue;
			}

			String textureName = entry.getValue();
			Class<?> entityClass = ClassLookup.getClass(EntityRef.PACKAGE_NAME,
					possibleClassName);

			// If the possibleClassName was correct, we can
			// add the entity as a creatable entity
			EntityRef.addTextureNameToEntityClassMapping(textureName,
					entityClass);
		}
	}

	public static Entity create(Class<?> entityClass, Vector2f vector2f,
			float sizeInput, boolean relativeSize) {
		if (entityClass == null) {
			System.out
					.println("Can't create an entity with an empty entityClass.");
			return null;
		}
		String textureName = (String) MathHelper.randInList(EntityRef
				.getTexturesFromEntityClass(entityClass));

		return create(textureName, entityClass, vector2f, sizeInput,
				relativeSize);
	}

	public static Entity create(String textureName, Vector2f location,
			float sizeInput, boolean relativeSize) {
		Class<?> entityClass = EntityRef
				.getEntityClassFromTextureName(textureName);
		if (entityClass == null) {

			App.print("Texture " + textureName
					+ " is missing a entity class mapping.");
			return null;
		}
		return create(textureName, entityClass, location, sizeInput,
				relativeSize);
	}

	public static Entity create(String textureName, Class<?> entityClass,
			Vector2f location, float sizeInput, boolean relativeSize) {

		float width = sizeInput;
		if (relativeSize) {
			int originalWidth = ((Texture) ContentManager.get(
					ContentType.texture, textureName)).getImageWidth();
			width = originalWidth * sizeInput;
		}

		Graphic graphic = new Graphic(textureName);
		EntitySetup setup = new EntitySetup();
		setup.set(EntityParam.graphic, graphic);
		setup.set(EntityParam.location, location);
		setup.set(EntityParam.width, width);
		setup.setEntityClassName(entityClass.getSimpleName());

		return buildFromSetup(setup);

	}

	private static Entity buildFromSetup(EntitySetup setup) {
		Entity newEntity = null;
		Class<?> entityClass = ClassLookup.getClass(EntityRef.PACKAGE_NAME,setup.getEntityClassName());
		try {
			newEntity = (Entity) getConstructor(entityClass).newInstance(setup);
		} catch (Exception e) {
			App.print("The class " + entityClass.toString()
					+ " has an implementation error: " + e.toString());
			return null;
		}

		if (newEntity == null) {
			App.print("Error: " + entityClass.toString() + " was created null.");
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

		return (String) MathHelper.randInList(EntityRef
				.getTexturesFromEntityClass(entityClass));
	}

	public static LinkedList<Entity> buildFromSetupCollection(
			Collection<EntitySetup> setups) {
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
			Collection<Entity> entities) {
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
