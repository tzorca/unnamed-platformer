package unnamed_platformer.globals;

public class GameRef {

	public static final float DEFAULT_SPRING_STRENGTH = 2f;

	public static final float DEFAULT_PLR_ACCELERATION = 2.5f;
	public static final float DEFAULT_PLR_DECELERATION = 4f;
	public static final float DEFAULT_PLR_MAX_SPEED = 5.5f;

	public static final float DEFAULT_PLR_JUMP_STRENGTH = 11f;

	public static final float DEFAULT_SHOOT_SPEED = 0.9f;
	public static final float DEFAULT_SHOOT_DELAY = 100;

	public final static float DEFAULT_WATER_SPEED_FACTOR = 0.5f;

	public static final float DEFAULT_FLAME_JUMP_INTERVAL = 3;
	public static final float DEFAULT_FLAME_SPEED = 4.5f;
	public static final float DEFAULT_FLAME_JUMP_HEIGHT = 192;
	public static final float DEFAULT_ORBIT_HAZARD_SPEED = 5f;
	
	
	public enum Flag {
		solid, hurtsOthers, obeysGravity, outOfPlay, tangible, levelGoal, player, invisible, editLogic, dissolvesOnContact
	}

	public enum InteractionResult {
		NO_RESULT,  X_COLLISION, Y_COLLISION, SKIP_PHYSICS
	}
}
