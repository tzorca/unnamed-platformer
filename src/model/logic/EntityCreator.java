package model.logic;

import java.awt.Point;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import model.entities.BreakableBlock;
import model.entities.Entity;
import model.entities.Goal;
import model.entities.Hazard;
import model.entities.PlatformPlayer;
import model.entities.SolidBlock;
import model.entities.SpringLike;
import model.parameters.ContentRef.ContentType;
import model.parameters.EntityRef;
import model.parameters.EntityRef.EntityType;

import org.newdawn.slick.opengl.Texture;

import app.ContentManager;

public class EntityCreator {

	public static void init() {
		Map<File, String> textureFiles = ContentManager
				.getFileNameMap(ContentType.texture);

		// TODO: Warning: This will cache all textures at the
		// start of the app. This may not be okay in the future.
		for (Entry<File, String> entry : textureFiles.entrySet()) {
			ContentManager.customCache(ContentType.texture, entry.getValue(),
					entry.getKey());

			String possibleTypeString = new File(entry.getKey().getParent())
					.getName();
			EntityType entityType = null;
			if (possibleTypeString != null) {
				try {
					entityType = EntityType.valueOf(possibleTypeString);
				} catch (Exception e) {
					// don't need to do anything here, possibleType is already
					// set to null

					System.out.println(e.toString());
				}
			}

			// If the type name was correct, we can add the entity as a
			// creatable entity
			if (entityType != null) {

				EntityRef.textureEntityTypeMap
						.put(entry.getValue(), entityType);
			}

		}
	}

	public static Entity create(String textureName, Point location,
			double sizeInput, boolean relativeSize) {
		EntityType type = EntityRef.textureEntityTypeMap.get(textureName);
		if (type == null) {

			System.out.println("Texture " + textureName
					+ " is missing a type mapping.");
			return null;
		}

		int width = (int) sizeInput;
		if (relativeSize) {
			int originalWidth = ((Texture) ContentManager.get(
					ContentType.texture, textureName)).getImageWidth();
			width = (int) (originalWidth * sizeInput);
		}

		Entity newEntity = null;

		switch (type) {
		case BreakableBlock:
			newEntity = new BreakableBlock(textureName, location, width);
			break;
		case Goal:
			newEntity = new Goal(textureName, location);
			break;
		case PlatformPlayer:
			newEntity = new PlatformPlayer(textureName, location);
			break;
		case SolidBlock:
			newEntity = new SolidBlock(textureName, location, width);
			break;
		case SpringLike:
			newEntity = new SpringLike(textureName, location);
			break;
		case Hazard:
			newEntity = new Hazard(textureName, location, width);
			break;
		default:
			System.out.println("The entity type " + type.toString()
					+ " has not yet been implemented.");
			return null;

		}
		return newEntity;
	}

	public static Entity create(String textureName, Point location) {
		return create(textureName, location, 1, true);
	}

	public static boolean hasMapping(String texName) {
		return EntityRef.textureEntityTypeMap.containsKey(texName);
	}

	public static Set<String> listTextureNames() {
		return EntityRef.textureEntityTypeMap.keySet();
	}

}
