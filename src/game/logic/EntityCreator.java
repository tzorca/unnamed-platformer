package game.logic;

import game.entities.BreakableBlock;
import game.entities.Entity;
import game.entities.Goal;
import game.entities.Hazard;
import game.entities.PlatformPlayer;
import game.entities.SolidBlock;
import game.entities.Spikes;
import game.entities.SpringLike;
import game.parameters.ContentRef.ContentType;
import game.parameters.EntityRef;
import game.parameters.EntityRef.EntityType;
import game.structures.Graphic;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
					// don't need to do anything here, entityType is already
					// set to null

					// System.out.println(e.toString());
				}
			}

			// If the type name was correct, we can add the entity as a
			// creatable entity
			if (entityType != null) {

				EntityRef.textureEntityTypeMap
						.put(entry.getValue(), entityType);

				if (!EntityRef.entityTypeTextureMap.containsKey(entityType)) {
					EntityRef.entityTypeTextureMap.put(entityType,
							new ArrayList<String>());
				}
				EntityRef.entityTypeTextureMap.get(entityType).add(
						entry.getValue());
			}

		}
	}

	public static Entity create(EntityType type, Point location,
			double sizeInput, boolean relativeSize) {
		if (type == null) {
			System.out.println("Can't create an entity with an empty type.");
			return null;
		}
		String textureName = (String) MathHelper
				.randInList(EntityRef.entityTypeTextureMap.get(type));

		return create(textureName, type, location, sizeInput, relativeSize);
	}

	public static Entity create(String textureName, Point location,
			double sizeInput, boolean relativeSize) {
		EntityType type = EntityRef.textureEntityTypeMap.get(textureName);
		if (type == null) {

			System.out.println("Texture " + textureName
					+ " is missing a type mapping.");
			return null;
		}
		return create(textureName, type, location, sizeInput, relativeSize);
	}

	public static Entity create(String textureName, EntityType type,
			Point location, double sizeInput, boolean relativeSize) {

		int width = (int) sizeInput;
		if (relativeSize) {
			int originalWidth = ((Texture) ContentManager.get(
					ContentType.texture, textureName)).getImageWidth();
			width = (int) (originalWidth * sizeInput);
		}

		Entity newEntity = null;

		Graphic graphic = new Graphic(textureName);

		switch (type) {
		case BreakableBlock:
			newEntity = new BreakableBlock(graphic, location, width);
			break;
		case Goal:
			newEntity = new Goal(graphic, location);
			break;
		case PlatformPlayer:
			newEntity = new PlatformPlayer(graphic, location);
			break;
		case SolidBlock:
			newEntity = new SolidBlock(graphic, location, width);
			break;
		case SpringLike:
			newEntity = new SpringLike(graphic, location);
			break;
		case Hazard:
			newEntity = new Hazard(graphic, location, width);
			break;
		case Spikes:
			newEntity = new Spikes(graphic, location, width);
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

	public static Entity create(String textureName, Point location, int width) {
		return create(textureName, location, width, false);
	}

	public static String chooseTextureFromType(EntityType type) {
		if (!EntityRef.entityTypeTextureMap.containsKey(type)) {
			return null;
		}

		return (String) MathHelper.randInList(EntityRef.entityTypeTextureMap
				.get(type));
	}

}
