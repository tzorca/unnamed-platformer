package unnamed_platformer.globals;

public final class GameRef
{

	public static final float DEFAULT_SPRING_STRENGTH = 2.2f;

	public static final float DEFAULT_PLR_ACCELERATION = 0.7f;
	public static final float DEFAULT_PLR_DECELERATION = 2.1f;
	public static final float DEFAULT_PLR_MAX_SPEED = 6.0f;

	public static final float DEFAULT_PLR_JUMP_STRENGTH = 8f;

	public static final float DEFAULT_SHOOT_SPEED = 0.7f;
	public static final float DEFAULT_SHOOT_DELAY = 150;

	public final static float DEFAULT_WATER_SPEED_FACTOR = 0.5f;
	public final static float DEFAULT_FAST_REGION_SPEED_FACTOR = 2f;

	public static final float DEFAULT_FLAME_JUMP_INTERVAL = 3;
	public static final float DEFAULT_FLAME_SPEED = 1.5f;
	public static final float DEFAULT_FLAME_JUMP_HEIGHT = 192;
	public static final float DEFAULT_ORBIT_HAZARD_SPEED = 5f;

	public static final int DEFAULT_MAX_HEALTH = 4;
	public static final int MAX_PLR_HEALTH = 8;

	public static final float TEMP_INVULNERABILITY_SECONDS = 1.5f;

	public static final float FLASH_INTERVAL = 0.05f;

	public enum Flag {
		SOLID, HURTS_OTHERS, OBEYS_GRAVITY, OUT_OF_PLAY, TANGIBLE, PLAYER, INVISIBLE, FOLLOWS_SLOPES, ALWAYS_INTERACT
	}
	
}
