package unnamed_platformer.game.entities;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.dynamics.control_mechanisms.Control_HorizontalMove;
import unnamed_platformer.game.dynamics.control_mechanisms.Control_Jump;
import unnamed_platformer.game.dynamics.control_mechanisms.Control_Shoot;
import unnamed_platformer.game.parameters.PhysicsRef;
import unnamed_platformer.game.parameters.Ref.Flag;
import unnamed_platformer.game.structures.Graphic;

public class PlatformPlayer extends ActiveEntity {
	private static final long serialVersionUID = 8624310770832698957L;

	public PlatformPlayer(Graphic graphic, Vector2f pos) {
		super(graphic, pos);
	}

	public PlatformPlayer(Graphic graphic, Vector2f pos, int width) {
		super(graphic, pos, width);
	}

	Control_HorizontalMove hzMoveBehaviour;
	Control_Jump jumpBehaviour;
	Control_Shoot shootBehaviour;

	protected void defaultActiveSetup() {
		hzMoveBehaviour = new Control_HorizontalMove(this,
				PhysicsRef.DEFAULT_PLR_ACCELERATION,
				PhysicsRef.DEFAULT_PLR_DECELERATION,
				PhysicsRef.DEFAULT_PLR_MAX_SPEED);

		jumpBehaviour = new Control_Jump(this,
				PhysicsRef.DEFAULT_PLR_JUMP_STRENGTH);
		shootBehaviour = new Control_Shoot(this, new Beam(new Graphic("laser"),
				new Vector2f(0, 0)), PhysicsRef.DEFAULT_SHOOT_SPEED,
				PhysicsRef.DEFAULT_SHOOT_DELAY);

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
