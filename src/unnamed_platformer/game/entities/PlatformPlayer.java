package unnamed_platformer.game.entities;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.TimeManager;
import unnamed_platformer.game.behaviours.Ctrl_HorizontalMove;
import unnamed_platformer.game.behaviours.Ctrl_Jump;
import unnamed_platformer.game.behaviours.Ctrl_Shoot;
import unnamed_platformer.game.other.EntitySetup;
import unnamed_platformer.globals.EntityRef.EntityParam;
import unnamed_platformer.globals.GameRef;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.view.Graphic;

//TODO: Fix launched projectiles to have constant speed
public class PlatformPlayer extends ActiveEntity
{
	Ctrl_HorizontalMove hzMoveBehaviour;
	Ctrl_Jump jumpBehaviour;
	Ctrl_Shoot shootBehaviour;

	int health = GameRef.DEFAULT_MAX_HEALTH;
	private boolean invulnerable;

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
		}

	}

	public void death() {
		returnToStart();
		health = GameRef.DEFAULT_MAX_HEALTH;
	}

	public int getHealth() {
		return health;
	}

	public void update() {
		super.update();

		if (invulnerable
				&& TimeManager.periodElapsed(this, TEMP_INVULNERABILITY,
						GameRef.TEMP_INVULNERABILITY_SECONDS)) {
			invulnerable = false;
		}
	}

	public void damage() {
		if (!invulnerable) {
			addHealth(-1);
			invulnerable = true;
			TimeManager.periodElapsed(this, TEMP_INVULNERABILITY,
					GameRef.TEMP_INVULNERABILITY_SECONDS);
		}
	}

	private static final String TEMP_INVULNERABILITY = "tempInvulnerability";

}
