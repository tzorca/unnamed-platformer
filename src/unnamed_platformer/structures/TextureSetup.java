package unnamed_platformer.structures;

import java.io.Serializable;

public class TextureSetup implements Serializable {
	private static final long serialVersionUID = -5456602392209992861L;
	
	private CollisionShapeOption collisionShapeOption;
	
	public enum CollisionShapeOption {
		none, rectangle, circle, polygon
	}

	public TextureSetup(String collisionShape) {
		this.collisionShapeOption = CollisionShapeOption.valueOf(collisionShape);
	}
	
	public CollisionShapeOption getCollisionShape() {
		return collisionShapeOption;
	}

}
