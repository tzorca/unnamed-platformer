package model.entities;

import java.awt.Point;

import model.Ref.Flag;
import model.dynamics.control_mechanisms.Control_HorizontalMove;
import model.dynamics.control_mechanisms.Control_Jump;
import model.dynamics.control_mechanisms.Control_Shoot;
import model.dynamics.interactions.NegativeHazardReaction;

public class PlatformPlayer extends ActiveEntity {
	private static final long serialVersionUID = 8624310770832698957L;

	Control_HorizontalMove hzMoveBehaviour = new Control_HorizontalMove(this,
			3f);

	Control_Jump jumpBehaviour = new Control_Jump(this, 5, 0.5);
	Control_Shoot shootBehaviour = new Control_Shoot(this, new Beam(new Point(
			0, 0)), 6, 100);

	public PlatformPlayer(String imageName, Point startPos) {
		super(imageName, startPos);

		addSpProps();
	}

	public PlatformPlayer(String imageName, Point startPos, float speed,
			double jumpStrength, double jumpTime) {
		super(imageName, startPos);
		setSpeed(speed);
		setJumpStrength(jumpStrength);
		setJumpTime(jumpTime);

		addSpProps();
	}

	private void addSpProps() {
		this.interactions.add(new NegativeHazardReaction(this));
		this.physics.addControlMechanism(hzMoveBehaviour);
		this.physics.addControlMechanism(jumpBehaviour);
		this.physics.addControlMechanism(shootBehaviour);
		this.setFlag(Flag.obeysGravity, true);
		this.setFlag(Flag.tangible, true);
		this.setFlag(Flag.player, true);
	}

	public float getSpeed() {
		return hzMoveBehaviour.getSpeed();
	}

	public void setSpeed(float speed) {
		hzMoveBehaviour.setSpeed(speed);
	}

	public double getJumpStrength() {
		return jumpBehaviour.getJumpStrength();
	}

	public void setJumpStrength(double jumpStrength) {
		jumpBehaviour.setJumpStrength(jumpStrength);
	}

	public double getJumpTime() {
		return jumpBehaviour.getJumpTime();
	}

	public void setJumpTime(double jumpTime) {
		jumpBehaviour.setJumpTime(jumpTime);
	}

}
