package unnamed_platformer.game.entities;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.EntitySetup;
import unnamed_platformer.game.ctrl_methods.Control_HorizontalMove;
import unnamed_platformer.game.ctrl_methods.Control_Jump;
import unnamed_platformer.game.ctrl_methods.Control_Shoot;
import unnamed_platformer.globals.EntityRef.EntityParam;
import unnamed_platformer.globals.GameRef;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.structures.Graphic;

//TODO: Fix launched projectiles to have constant speed
public class PlatformPlayer extends ActiveEntity {
	private static final long serialVersionUID = 8624310770832698957L;

	Control_HorizontalMove hzMoveBehaviour;
	Control_Jump jumpBehaviour;
	Control_Shoot shootBehaviour;

	public PlatformPlayer(EntitySetup entitySetup) {
		super(entitySetup);

		hzMoveBehaviour = new Control_HorizontalMove(this,
				GameRef.DEFAULT_PLR_ACCELERATION,
				GameRef.DEFAULT_PLR_DECELERATION,
				GameRef.DEFAULT_PLR_MAX_SPEED);

		jumpBehaviour = new Control_Jump(this,
				GameRef.DEFAULT_PLR_JUMP_STRENGTH);

		EntitySetup setup = new EntitySetup();
		setup.set(EntityParam.graphic, new Graphic("laser"));
		setup.set(EntityParam.location, new Vector2f(0, 0));

		shootBehaviour = new Control_Shoot(this, new Beam(setup),
				GameRef.DEFAULT_SHOOT_SPEED, GameRef.DEFAULT_SHOOT_DELAY);

		this.getPhysics().addControlMechanism(hzMoveBehaviour);
		this.getPhysics().addControlMechanism(jumpBehaviour);
		this.getPhysics().addControlMechanism(shootBehaviour);
		this.setFlag(Flag.obeysGravity, true);
		this.setFlag(Flag.tangible, true);
		this.setFlag(Flag.player, true);

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

}
