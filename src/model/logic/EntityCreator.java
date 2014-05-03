package model.logic;

import java.awt.Point;

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
import model.parameters.PhysicsRef;

import org.newdawn.slick.opengl.Texture;

import app.ContentManager;

public class EntityCreator {

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

			newEntity = new PlatformPlayer(textureName, location,
					PhysicsRef.DEFAULT_PLR_SPEED,
					PhysicsRef.DEFAULT_PLR_JUMP_STRENGTH,
					PhysicsRef.DEFAULT_PLR_JUMP_TIME);
			break;
		case SolidBlock:
			newEntity = new SolidBlock(textureName, location, width);
			break;
		case SpringLike:
			newEntity = new SpringLike(textureName, location,
					PhysicsRef.Orientation.UP,
					PhysicsRef.DEFAULT_SPRING_STRENGTH);
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

}
