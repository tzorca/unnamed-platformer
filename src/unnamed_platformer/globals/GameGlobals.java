package unnamed_platformer.globals;

public final class GameGlobals
{
	public static final float SPRING_STRENGTH = 9.5f;

	public static final float PLAYER_ACCELERATION = 0.7f;
	public static final float PLAYER_DECELERATION = 2.1f;
	public static final float PLAYER_MAX_SPEED = 6.0f;

	public static final float PLAYER_JUMP_STRENGTH = 8.25f;

	public static final float PLAYER_PROJECTILE_SPEED = 8f;
	public static final float PLAYER_PROJECTILE_DELAY = 200;
	public static final float PLAYER_SHOOT_VARIABILITY = 0.05f;

	public final static float WATER_SPEED_MODIFIER = 0.5f;
	public final static float FAST_REGION_SPEED_MODIFIER = 1.8f;

	public static final float FLAME_JUMP_INTERVAL = 4;
	public static final float FLAME_SPEED = 5f;
	public static final float FLAME_JUMP_HEIGHT = 256;
	public static final float ORBITING_HAZARD_SPEED = 5f;

	public static final int PLAYER_INITIAL_HEALTH = 4;
	public static final int PLAYER_MAX_HEALTH = 16;

	public static final float TEMPORARY_INVULNERABILITY_SECONDS = 1.5f;

	public static final float INVULNERABILITY_FLASH_INTERVAL = 0.1f;

	public static final float MOTION_BOX_DISTANCE = 192;
	public static final float MOTION_BOX_SPEED = 2f;

	public static final int TEXTURE_COLLISION_BLUR_ITERATIONS = 2;
	public static final float TEXTURE_COLLISION_SIZE_MODIFIER = .95f;

	public static final float BUMPER_STRENGTH = 6f;
	public static final float BUMPER_CONTROL_LOCK_TIME = 0.2f;

	public enum Flag {
		SOLID, HURTS_OTHERS, OBEYS_GRAVITY, OUT_OF_PLAY, TANGIBLE, PLAYER, INVISIBLE, FOLLOWS_SLOPES, ALWAYS_INTERACT, INACTIVE_UNTIL_PLAYER_DEATH, LOCK_CONTROLS
	}

	public enum EntityParam {
		GRAPHIC, LOCATION, ORIENTATION, UNUSED_A, UNUSED_B, SIZE_STRATEGY
	}

}
