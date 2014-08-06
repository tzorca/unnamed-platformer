package unnamed_platformer.structures;

public class TextureSetup {

	private CollisionShapeType collisionShapeType;
	
	public enum CollisionShapeType {
		none, rectangle, circle, polygon
	}

	public TextureSetup(String collisionShape) {
		this.collisionShapeType = CollisionShapeType.valueOf(collisionShape);
	}
	
	public CollisionShapeType getCollisionShapeType() {
		return this.collisionShapeType;
	}

}
