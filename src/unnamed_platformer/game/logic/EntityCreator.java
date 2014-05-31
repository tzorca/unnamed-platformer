package unnamed_platformer.game.logic;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.app.App;
import unnamed_platformer.app.ClassLookup;
import unnamed_platformer.app.ContentManager;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.parameters.EntityRef;
import unnamed_platformer.game.parameters.ContentRef.ContentType;
import unnamed_platformer.game.structures.Graphic;

public class EntityCreator {

	// Setup texture entity subclass mappings
	// and load textures and binarypixelgrid
	public static void init() {
		Map<File, String> textureFiles = ContentManager
				.getFileNameMap(ContentType.texture);

		for (Entry<File, String> entry : textureFiles.entrySet()) {

			// TODO: Warning: This will cache all textures at the
			// start of the app. This may not be okay in the future.
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
			Class<?> entityClass = ClassLookup.getClass(
					EntityRef.PACKAGE_NAME, possibleClassName);

			// If the possibleClassName was correct, we can
			// add the entity as a creatable entity
			EntityRef.addTextureNameToEntityClassMapping(textureName,
					entityClass);

		}
	}

	public static Entity create(Class<?> entityClass, Vector2f vector2f,
			double sizeInput, boolean relativeSize) {
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
			double sizeInput, boolean relativeSize) {
		Class<?> entityClass = EntityRef
				.getEntityClassFromTextureName(textureName);
		if (entityClass == null) {

			App.print("Texture " + textureName
					+ " is missing a entityClass mapping.");
			return null;
		}
		return create(textureName, entityClass, location, sizeInput,
				relativeSize);
	}

	public static Entity create(String textureName, Class<?> entityClass,
			Vector2f location, double sizeInput, boolean relativeSize) {

		int width = (int) sizeInput;
		if (relativeSize) {
			int originalWidth = ((Texture) ContentManager.get(
					ContentType.texture, textureName)).getImageWidth();
			width = (int) (originalWidth * sizeInput);
		}

		Entity newEntity = null;

		Graphic graphic = new Graphic(textureName);

		try {
			newEntity = (Entity) entityClass.getConstructor(Graphic.class,
					Vector2f.class, int.class).newInstance(graphic, location,
					width);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			App.print("The class " + entityClass.toString()
					+ " has an implementation error: " + e.toString());
			return null;

		}
		if (newEntity == null) {
			App.print("Error: " + entityClass.toString() + "was created null.");
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

}
