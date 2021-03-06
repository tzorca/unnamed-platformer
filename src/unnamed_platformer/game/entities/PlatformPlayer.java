package unnamed_platformer.game.entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.AudioManager;
import unnamed_platformer.app.TimeManager;
import unnamed_platformer.game.behaviours.Ctrl_HorizontalMove;
import unnamed_platformer.game.behaviours.Ctrl_Jump;
import unnamed_platformer.game.behaviours.Ctrl_Shoot;
import unnamed_platformer.game.editor.EntitySetup;
import unnamed_platformer.game.zones.World;
import unnamed_platformer.globals.GameGlobals;
import unnamed_platformer.globals.GameGlobals.EntityParam;
import unnamed_platformer.globals.GameGlobals.Flag;
import unnamed_platformer.view.Graphic;

public class PlatformPlayer extends ActiveEntity
{
	Ctrl_HorizontalMove hzMoveBehaviour;
	Ctrl_Jump jumpBehaviour;
	Ctrl_Shoot shootBehaviour;

	int health = GameGlobals.PLAYER_INITIAL_HEALTH;
	private boolean invulnerable;
	private boolean flashStatus;

	public PlatformPlayer(EntitySetup entitySetup) {
		super(entitySetup);

		hzMoveBehaviour = new Ctrl_HorizontalMove(this,
				GameGlobals.PLAYER_ACCELERATION,
				GameGlobals.PLAYER_DECELERATION, GameGlobals.PLAYER_MAX_SPEED);

		jumpBehaviour = new Ctrl_Jump(this, GameGlobals.PLAYER_JUMP_STRENGTH);

		EntitySetup projectileSetup = new EntitySetup();
		projectileSetup.set(EntityParam.GRAPHIC, new Graphic("laser"));
		projectileSetup.set(EntityParam.LOCATION, new Vector2f(0, 0));

		shootBehaviour = new Ctrl_Shoot(this, new Beam(projectileSetup),
				GameGlobals.PLAYER_PROJECTILE_SPEED, GameGlobals.PLAYER_PROJECTILE_DELAY,
				GameGlobals.PLAYER_SHOOT_VARIABILITY);

		getPhysics();
		physics.addControlMechanism(hzMoveBehaviour);
		physics.addControlMechanism(jumpBehaviour);
		physics.addControlMechanism(shootBehaviour);
		setFlag(Flag.OBEYS_GRAVITY, true);
		setFlag(Flag.TANGIBLE, true);
		setFlag(Flag.PLAYER, true);
		
		// Fix for grid-based platform sticking bug
		getPhysics().setYVelocity(-1);

		zIndex = 2;
	}

	public float getSpeed() {
		return hzMoveBehaviour.getAcceleration();
	}

	public void setSpeed(float speed) {
		hzMoveBehaviour.setAcceleration(speed);
	}

	public double getJumpStrength() {
		return jumpBehaviour.getJumpStrength();
	}

	public void setJumpStrength(float jumpStrength) {
		jumpBehaviour.setJumpStrength(jumpStrength);
	}

	public void addHealth(int healthDelta) {
		if (health + healthDelta > GameGlobals.PLAYER_MAX_HEALTH) {
			return;
		}

		health += healthDelta;

		if (health <= 0) {
			death();
		} else if (healthDelta < 0) {
			AudioManager.playSample("hit");
		} else if (healthDelta > 0) {
			AudioManager.playSample("energy");
		}
	}

	public void returnToStart() {
		super.returnToStart();
		
		// Fix for grid-based platform sticking bug
		getPhysics().setYVelocity(-1);
	}
	
	public void death() {
		AudioManager.playSample("death");
		returnToStart();
		health = GameGlobals.PLAYER_INITIAL_HEALTH;
		flashStatus = false;
		invulnerable = false;
		World.getCurrentLevel().signalPlayerDeath();
	}

	public int getHealth() {
		return health;
	}

	public void update() {
		super.update();

		if (invulnerable) {
			if (TimeManager.periodElapsed(this, STR_TEMP_INVULNERABILITY,
					GameGlobals.TEMPORARY_INVULNERABILITY_SECONDS)) {
				invulnerable = false;
				flashStatus = false;
			} else {
				// flash
				if (TimeManager.periodElapsed(this, STR_FLASHING,
						GameGlobals.INVULNERABILITY_FLASH_INTERVAL)) {
					flashStatus = !flashStatus;
				}
			}
		}

		// Falling off the map
		// TODO: Refactor this into a generic way of checking if an entity is
		// out of view or level bounds
		if (this.getOriginalBox().getMinY() > World.getCurrentLevel().getRect()
				.getMaxY()) {
			death();
		}

		graphic.color = flashStatus ? new Color(0xff, 0xff, 0xff, 0xaa)
				: Color.white;
	}

	public void damage() {
		if (!invulnerable) {
			addHealth(-1);
			graphic.color = Color.red;
			invulnerable = true;
			TimeManager.periodElapsed(this, STR_TEMP_INVULNERABILITY,
					GameGlobals.TEMPORARY_INVULNERABILITY_SECONDS);
		}
	}

	private static final String STR_TEMP_INVULNERABILITY = "tempInvulnerability";
	private static final String STR_FLASHING = "flashing";

}
