package unnamed_platformer.game.entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.TimeManager;
import unnamed_platformer.game.behaviours.Ctrl_HorizontalMove;
import unnamed_platformer.game.behaviours.Ctrl_Jump;
import unnamed_platformer.game.behaviours.Ctrl_Shoot;
import unnamed_platformer.game.other.EntitySetup;
import unnamed_platformer.game.other.World;
import unnamed_platformer.globals.EntityRef.EntityParam;
import unnamed_platformer.globals.GameRef;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.res_mgt.SoundManager;
import unnamed_platformer.view.Graphic;

//TODO: Fix launched projectiles to have constant speed
public class PlatformPlayer extends ActiveEntity
{
	Ctrl_HorizontalMove hzMoveBehaviour;
	Ctrl_Jump jumpBehaviour;
	Ctrl_Shoot shootBehaviour;

	int health = GameRef.DEFAULT_MAX_HEALTH;
	private boolean invulnerable;
	private boolean flashStatus;

	public PlatformPlayer(EntitySetup entitySetup) {
		super(entitySetup);

		hzMoveBehaviour = new Ctrl_HorizontalMove(this,
				GameRef.DEFAULT_PLR_ACCELERATION,
				GameRef.DEFAULT_PLR_DECELERATION, GameRef.DEFAULT_PLR_MAX_SPEED);

		jumpBehaviour = new Ctrl_Jump(this, GameRef.DEFAULT_PLR_JUMP_STRENGTH);

		EntitySetup setup = new EntitySetup();
		setup.set(EntityParam.GRAPHIC, new Graphic("laser"));
		setup.set(EntityParam.LOCATION, new Vector2f(0, 0));

		shootBehaviour = new Ctrl_Shoot(this, new Beam(setup),
				GameRef.DEFAULT_SHOOT_SPEED, GameRef.DEFAULT_SHOOT_DELAY);

		getPhysics();
		physics.addControlMechanism(hzMoveBehaviour);
		physics.addControlMechanism(jumpBehaviour);
		physics.addControlMechanism(shootBehaviour);
		setFlag(Flag.OBEYS_GRAVITY, true);
		setFlag(Flag.TANGIBLE, true);
		setFlag(Flag.PLAYER, true);

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
		if (health + healthDelta > GameRef.MAX_PLR_HEALTH) {
			return;
		}

		health += healthDelta;

		if (health <= 0) {
			death();
		} else if (healthDelta < 0) {
			SoundManager.playSample("hit");
		} else if (healthDelta > 0){
			SoundManager.playSample("energy");
		}
	}

	public void death() {
		SoundManager.playSample("death");
		returnToStart();
		health = GameRef.DEFAULT_MAX_HEALTH;
		flashStatus = false;
		invulnerable = false;
	}

	public int getHealth() {
		return health;
	}

	public void update() {
		super.update();

		if (invulnerable) {
			if (TimeManager.periodElapsed(this, STR_TEMP_INVULNERABILITY,
					GameRef.TEMP_INVULNERABILITY_SECONDS)) {
				invulnerable = false;
				flashStatus = false;
			} else {
				// flash
				if (TimeManager.periodElapsed(this, STR_FLASHING,
						GameRef.FLASH_INTERVAL)) {
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
					GameRef.TEMP_INVULNERABILITY_SECONDS);
		}
	}

	private static final String STR_TEMP_INVULNERABILITY = "tempInvulnerability";
	private static final String STR_FLASHING = "flashing";

}
