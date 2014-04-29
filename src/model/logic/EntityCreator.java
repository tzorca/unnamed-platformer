package model.logic;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import model.Ref;
import model.entities.BreakableBlock;
import model.entities.Entity;
import model.entities.Goal;
import model.entities.Hazard;
import model.entities.PlatformPlayer;
import model.entities.SolidBlock;
import model.entities.SpringLike;

import org.newdawn.slick.opengl.Texture;

import app.ContentManager;
import app.ContentManager.ContentType;

public class EntityCreator {

	public enum EntityType {
		BreakableBlock, Goal, PlatformPlayer, SpringLike, SolidBlock, Bonus, Hazard, SlowMovementRegion
	}

	public static final Map<String, EntityType> textureEntityTypeMap = new HashMap<String, EntityType>();
	static {
		textureEntityTypeMap.put("black", EntityType.SolidBlock);
		textureEntityTypeMap.put("white", EntityType.BreakableBlock);
		textureEntityTypeMap.put("flag", EntityType.Goal);
		textureEntityTypeMap.put("gem", EntityType.Bonus);
		textureEntityTypeMap.put("lava", EntityType.Hazard);
		textureEntityTypeMap.put("player", EntityType.PlatformPlayer);
		textureEntityTypeMap.put("spikes", EntityType.Hazard);
		textureEntityTypeMap.put("spring", EntityType.SpringLike);
		//textureEntityTypeMap.put("water", EntityType.SlowMovementRegion);
	}

	public static Entity create(String textureName, Point location,
			double sizeInput, boolean relativeSize) {
		EntityType type = textureEntityTypeMap.get(textureName);
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
					Ref.DEFAULT_PLR_SPEED, Ref.DEFAULT_PLR_JUMP_STRENGTH,
					Ref.DEFAULT_PLR_JUMP_TIME);
			break;
		case SolidBlock:
			newEntity = new SolidBlock(textureName, location, width);
			break;
		case SpringLike:
			newEntity = new SpringLike(textureName, location,
					Ref.Orientation.UP, Ref.DEFAULT_SPRING_STRENGTH);
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
		return textureEntityTypeMap.containsKey(texName);
	}

}
